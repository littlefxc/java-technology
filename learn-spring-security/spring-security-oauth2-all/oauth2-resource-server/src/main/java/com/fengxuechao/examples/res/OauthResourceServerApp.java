package com.fengxuechao.examples.res;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/17
 */
@RestController
@SpringBootApplication
public class OauthResourceServerApp {

    public static void main(String[] args) {
        SpringApplication.run(OauthResourceServerApp.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return "hello";
    }
}
