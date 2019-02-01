package com.fengxuechao.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用 Spring Boot WebFlux 的注解控制层技术创建一个 CRUD WebFlux 应用
 *
 * @author fengxuechao
 */
@SpringBootApplication
public class WebFluxMongoApp {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxMongoApp.class, args);
    }
}
