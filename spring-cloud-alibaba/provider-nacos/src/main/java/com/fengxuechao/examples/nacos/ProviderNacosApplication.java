package com.fengxuechao.examples.nacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
public class ProviderNacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderNacosApplication.class, args);
    }

    @Slf4j
    @RestController
    static class ProviderController {

        @GetMapping("/hello")
        public String hello(@RequestParam String name) {
            if (log.isDebugEnabled())
                log.debug("invokd name = {}", name);
            return "hello, " + name;
        }

    }
}
