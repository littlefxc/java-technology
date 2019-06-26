package com.littlefxc.example.service;

import com.littlefxc.example.dao.CityDao;
import com.littlefxc.example.domain.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
 * @author fengxuechao
 * @date 2019/1/9
 **/
@Service
public class RedisCacheService {

    @Autowired
    private CityDao cityDao;

    /**
     * 保存 city
     *
     * @param city
     * @return
     */
    public City add(City city) {
        cityDao.save(city);
        return city;
    }

    /**
     * @return
     * @see {@link Cacheable}
     * 声明Spring在调用方法之前，首先应该在缓存中查找方法的返回值。如果这个值能够找到，就会返回存储的值，否则的话，这个方法就会被调用，返回值会放在缓存之中。
     */
    @Cacheable(value = "city", key = "#id")
    public City getUser(Integer id) {
        return cityDao.findCityById(id);
    }

    /**
     * 表明Spring应该将方法的返回值放到缓存中，在方法的调用前并不会检查缓存，方法始终都会被调用
     *
     * @return
     * @see {@link CachePut}
     */
    @CachePut(value = "city", key = "#city.id", condition = "#city.id!=null")
    public City updateCity(City city) {
        cityDao.update(city);
        return city;
    }

    /**
     * 表明Spring应该在缓存中清除一个或多个条目
     *
     * @return
     * @see {@link CacheEvict}
     */
    @CacheEvict(value = {"city"}, key = "#id")
    public void delete(Integer id) {
        cityDao.delete(id);
    }

    /**
     * 这是一个分组的注解，能够同时应用多个其他的缓存注解
     *
     * @return
     * @see {@link Caching}
     */
    @Caching(cacheable = @Cacheable(value = "city", key = "#id"))
    public City caching(Integer id) {
        return cityDao.findCityById(id);
    }
}
