package com.fengxuechao.examples.sso.res;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengxuechao
 * @date 2019/3/26
 */
@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class ResServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ResServerApp.class, args);
    }

    // 资源API
    @RequestMapping("/api/userinfo")
    public ResponseEntity<Map> getUserInfo() {

        String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user + "@test.com";
        Map<String, String> map = new HashMap<>();
        map.put("name", user);
        map.put("email", email);
        return ResponseEntity.ok(map);
    }
}
