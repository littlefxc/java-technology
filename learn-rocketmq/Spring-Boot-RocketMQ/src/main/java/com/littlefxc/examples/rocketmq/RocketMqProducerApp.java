package com.littlefxc.examples.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fengxuechao
 * @date 2019-02-15
 */
@Slf4j
@SpringBootApplication
public class RocketMqProducerApp {

    public static void main(String[] args) {
        SpringApplication.run(RocketMqProducerApp.class, args);
    }
}
