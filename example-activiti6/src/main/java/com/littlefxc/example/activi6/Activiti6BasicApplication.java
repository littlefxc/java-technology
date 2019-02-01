package com.littlefxc.example.activi6;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.apache.camel.spring.SpringCamelContext;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author fengxuechao
 */
@MapperScan("com.littlefxc.example.activi6.dao")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Activiti6BasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(Activiti6BasicApplication.class, args);
    }

    @Bean
    public SpringCamelContext camelContext() {
        return new SpringCamelContext();
    }
}
