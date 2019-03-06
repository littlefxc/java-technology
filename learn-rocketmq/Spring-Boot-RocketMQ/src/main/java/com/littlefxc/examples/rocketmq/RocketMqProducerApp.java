package com.littlefxc.examples.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.rocketmq.starter.annotation.EnableRocketMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author fengxuechao
 * @date 2019-02-15
 */
@Slf4j
@SpringBootApplication
@EnableRocketMQ
@RestController
public class RocketMqProducerApp {

    @Autowired
    private DefaultMQProducer producer;

    @Autowired
    private DefaultMQPullConsumer mqPullConsumer;

    public static void main(String[] args) {
        SpringApplication.run(RocketMqProducerApp.class, args);
    }

    /**
     * spring-boot-starter-rocketmq 只是初始化了生产者，但并没有启动生产者
     */
    @PostConstruct
    public void postConstruct() {
        try {
            if (mqPullConsumer != null) {
                mqPullConsumer.start();
            }
            if (producer != null) {
                producer.start();
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生产者消费者正常关闭可以防止消息丢失
     */
    @PreDestroy
    public void preDestroy() {
        if (mqPullConsumer != null) {
            mqPullConsumer.shutdown();
        }
        if (producer != null) {
            producer.shutdown();
        }
    }
}
