package com.fengxuechao.examples.integration;

import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/4/18
 * @title HelloWorld
 * @project_name spring-integration
 * @description TODO
 */
public class HelloWorld {

    public static void main(String[] args) {
//        SubscribableChannel messageChannel = new DirectChannel();
        SubscribableChannel messageChannel = new PublishSubscribeChannel();

        messageChannel.subscribe(msg -> {
            System.out.println("receive1: " + msg.getPayload());
        });

        messageChannel.subscribe(msg -> {
            System.out.println("receive2: " + msg.getPayload());
        });

        messageChannel.send(MessageBuilder.withPayload("msg from alibaba").build());
        messageChannel.send(MessageBuilder.withPayload("msg from alibaba").build());
    }
}
