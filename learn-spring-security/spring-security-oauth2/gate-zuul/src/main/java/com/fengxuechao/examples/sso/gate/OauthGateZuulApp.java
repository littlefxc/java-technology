package com.fengxuechao.examples.sso.gate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author fengxuechao
 * @date 2019/4/1
 */
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class OauthGateZuulApp {

    public static void main(String[] args) {
        SpringApplication.run(OauthGateZuulApp.class, args);
    }
}
