package com.littlefxc.examples.rocketmq.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;

/**
 * @author fengxuechao
 * @date 2019-02-15
 */
@Slf4j
@Configuration
public class Producer {

    private String producerGroup = "rmq-group";

    private String namesrvAddr = "192.168.212.75:9876;192.168.212.76:9876";

    @Bean
    public DefaultMQProducer defaultMQProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(namesrvAddr);
        try {
            producer.start();
            if (log.isInfoEnabled()) {
                log.info("DefaultMQProducer started");
            }
            return producer;
        } catch (MQClientException e) {
            log.error("DefaultMQProducer 启动失败", e);
        } finally {
            producer.shutdown();
        }
        return null;
    }
}
