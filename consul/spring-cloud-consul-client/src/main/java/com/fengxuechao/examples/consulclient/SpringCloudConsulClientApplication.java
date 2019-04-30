package com.fengxuechao.examples.consulclient;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudConsulClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConsulClientApplication.class, args);
    }

    //参照源码定义声名
    @Autowired(required = false)
    private TtlScheduler ttlScheduler;

    //重写register方法
    @Bean
    public GatewayRegister consulServiceRegistry(
            ConsulClient consulClient, ConsulDiscoveryProperties properties, HeartbeatProperties heartbeatProperties) {
        return new GatewayRegister(consulClient, properties, ttlScheduler, heartbeatProperties);
    }
}
