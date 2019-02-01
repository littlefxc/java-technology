package com.fengxuechao.example.handler;

import com.fengxuechao.example.domain.City;
import com.fengxuechao.example.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CityHandler {

    private final CityRepository cityRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    public CityHandler(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Mono<City> save(City city) {
        return cityRepository.save(city);
    }


    public Mono<City> findCityById(Long id) {

        // 从缓存中获取城市信息
        String key = "city_" + id;
        ValueOperations<String, City> operations = redisTemplate.opsForValue();

        // 缓存存在
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            City city = operations.get(key);

            log.info("CityHandler.findCityById() : 从缓存中获取了城市 >> " + city.toString());
            return Mono.create(cityMonoSink -> cityMonoSink.success(city));
        }

        // 从 MongoDB 中获取城市信息
        Mono<City> cityMono = cityRepository.findById(id);

        if (cityMono == null)
            return cityMono;

        // 插入缓存
        cityMono.subscribe(cityObj -> {
            operations.set(key, cityObj);
            log.info("CityHandler.findCityById() : 城市插入缓存 >> " + cityObj.toString());
        });

        return cityMono;
    }

    public Flux<City> findAllCity() {
        return cityRepository.findAll().cache();
    }

    public Mono<City> modifyCity(City city) {

        Mono<City> cityMono = cityRepository.save(city);

        // 缓存存在，删除缓存
        String key = "city_" + city.getId();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);

            log.info("CityHandler.modifyCity() : 从缓存中删除城市 ID >> " + city.getId());
        }

        return cityMono;
    }

    public Mono<Long> deleteCity(Long id) {

        cityRepository.deleteById(id);

        // 缓存存在，删除缓存
        String key = "city_" + id;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);

            log.info("CityHandler.deleteCity() : 从缓存中删除城市 ID >> " + id);
        }

        return Mono.create(cityMonoSink -> cityMonoSink.success(id));
    }
}