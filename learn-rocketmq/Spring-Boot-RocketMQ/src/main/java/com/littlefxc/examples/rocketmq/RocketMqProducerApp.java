package com.littlefxc.examples.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.rocketmq.starter.annotation.EnableRocketMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public static void main(String[] args) {
        SpringApplication.run(RocketMqProducerApp.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        try {
            if (producer != null) {
                producer.start();
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void preDestroy() {
        if (producer != null) {
            producer.shutdown();
        }
    }

    /**
     * 发送同步消息
     * @param msg
     * @throws Exception
     */
    @RequestMapping("send")
    public void send(String msg) throws Exception {
        for (int i = 0; i < 10; i++) {
            Message message = new Message(
                    "Topic_A",
                    "TagA",
                    ("同步消息 " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            producer.send(message);
        }
    }

}
