package com.littlefxc.spring.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
public class InMemoryAuthenticationApp {

    public static void main(String[] args) {
        SpringApplication.run(InMemoryAuthenticationApp.class, args);
    }
}