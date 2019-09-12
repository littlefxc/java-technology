package com.fengxuechao.amqp.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/9/12
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue queueHello() {
        return new Queue("hello");
    }

    @Bean
    public Queue queueObject() {
        return new Queue("object");
    }
}
