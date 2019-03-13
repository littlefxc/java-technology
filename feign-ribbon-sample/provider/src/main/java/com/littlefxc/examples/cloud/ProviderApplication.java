package com.littlefxc.examples.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用@EnableDiscoveryClient：服务发现
 */
@EnableDiscoveryClient
@RestController
@SpringBootApplication
public class ProviderApplication {

    private static Logger log = LoggerFactory.getLogger(ProviderApplication.class);

    ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap(16);

    @GetMapping(value = "/get")
    public String get(@RequestParam String key) {
        return "GET SUCCESS => key: "+ key +", value: " + hashMap.get(key);
    }

    @PostMapping(value = "/post")
    public String post(@RequestParam String key, @RequestParam String value) {
        hashMap.put(key, value);
        return "POST SUCCESS => key: "+ key +", value: " + hashMap.get(key);
    }

    @PutMapping(value = "/put")
    public String put(@RequestParam String key, @RequestParam String value) {
        hashMap.put(key, value);
        return "PUT SUCCESS => key: "+ key +", value: " + hashMap.get(key);
    }

    @DeleteMapping(value = "/delete")
    public String delete(@RequestParam String key) {
        Object remove = hashMap.remove(key);
        return "DELETE SUCCESS => value: " + remove;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}