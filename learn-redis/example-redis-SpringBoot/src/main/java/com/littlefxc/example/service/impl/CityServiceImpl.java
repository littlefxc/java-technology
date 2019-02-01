package com.littlefxc.example.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.littlefxc.example.dao.CityDao;
import com.littlefxc.example.domain.City;
import com.littlefxc.example.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author fengxuechao
 * @date 2018/12/27
 **/
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CityServiceImpl implements CityService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CityDao cityDao;

    private ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

    /**
     * 根据Id查询城市
     *
     * @param id
     * @return
     * @throws IOException
     */
    @Override
    public City findCityById(Integer id) throws IOException {
        String key = "s_city_" + id;
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey != null && !hasKey) {
            City city = cityDao.findCityById(id);
            if (log.isDebugEnabled()) {
                log.debug(city.toString());
            }
            if (city != null) {
                operations.set(key, objectMapper.writeValueAsString(city), 10, TimeUnit.SECONDS);
            }
            return city;
        }
        return objectMapper.readValue(operations.get(key), City.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public City saveCity(City city) {
        return cityDao.save(city) > 0 ? city : null;
    }

}
