package com.example.atomikos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * 研究分布式事务: spring-boot-starter-jta-atomikos
 * key.properties: druid加密公钥
 *
 * @author fengxuechao
 */
//@PropertySource("classpath:key.properties")
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
public class LearnJtaAtomikosApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnJtaAtomikosApplication.class, args);
    }
}
