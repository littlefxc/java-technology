package com.littlefxc.examples.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.rocketmq.starter.annotation.RocketMQListener;
import org.rocketmq.starter.annotation.RocketMQMessage;
import org.springframework.stereotype.Service;

/**
 * @author fengxuechao
 * @date 2019-02-15
 */
@Slf4j
@Service
@RocketMQListener(topic = "Topic_A")
public class MyConsumer {

     @RocketMQMessage(messageClass = String.class,tag = "TagA")
     public void onMessage(String message) {
         log.info("received message: {}", message);
     }
}