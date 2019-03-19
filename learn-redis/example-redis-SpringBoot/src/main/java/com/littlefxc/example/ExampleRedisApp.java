package com.littlefxc.example;

import com.littlefxc.example.service.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fengxuechao
 * @date 2018/12/27
 **/
@SpringBootApplication
public class ExampleRedisApp {

    @Autowired
    RedisCacheService redisCacheService;

    public static void main(String[] args) {
        SpringApplication.run(ExampleRedisApp.class, args);
    }
}
