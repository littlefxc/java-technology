package com.littlefxc.example.service;

import com.littlefxc.example.domain.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisCacheServiceTest {

    @Autowired
    RedisCacheService cacheService;

    @Test
    public void getUser() {
        System.out.println(cacheService.getUser(1));
    }

    @Test
    public void updateCity() {
        City city = new City();
        city.setId(1);
        city.setCityName("绍兴");
        city.setProvinceId(1);
        city.setDescription("");
        cacheService.updateCity(city);
        System.out.println(cacheService.getUser(1));
    }

    @Test
    public void delete() {
        cacheService.delete(1);
    }

    @Test
    public void caching() {
        System.out.println(cacheService.caching(1));
    }
}