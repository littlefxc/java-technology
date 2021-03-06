package com.littlefxc.examples.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.bind.annotation.*;

/**
 * 在spring MVC请求方法中使用调用 provider-api 依赖中的各个接口方法
 *
 * @author fengxuechao
 */
@EnableFeignClients
@EnableDiscoveryClient // 若注册中心是 eureka， 可以用@EnableEurekaClient
@SpringBootApplication
@RestController
public class ConsumerFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerFeignApplication.class, args);
    }

    @Autowired
    ProviderService service;

    @GetMapping(value = "/get")
    public String get(@RequestParam String key) {
        return service.get(key);
    }

    @PostMapping(value = "/post")
    public String post(@RequestParam String key, @RequestParam String value) {
        return service.post(key, value);
    }

    @PutMapping(value = "/put")
    public String put(@RequestParam String key, @RequestParam String value) {
        return service.put(key, value);
    }

    @DeleteMapping(value = "/delete")
    public String delete(@RequestParam String key) {
        return service.delete(key);
    }
}