package com.littlefxc.example;

import com.littlefxc.example.service.MusicService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author fengxuechao
 * @date 2018/12/27
 **/
@SpringBootApplication
public class ExampleRedisApp implements ApplicationRunner {

    @Autowired
    MusicService musicService;

    public static void main(String[] args) {
        SpringApplication.run(ExampleRedisApp.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("message: " + musicService.play("trumpet"));
        System.out.println("message: " + musicService.play("trumpet"));
        System.out.println("message: " + musicService.play("guitar"));
        System.out.println("message: " + musicService.play("guitar"));

        System.out.println("Done.");
    }
}
