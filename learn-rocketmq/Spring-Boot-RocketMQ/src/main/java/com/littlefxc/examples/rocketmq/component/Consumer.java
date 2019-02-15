package com.littlefxc.examples.rocketmq.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengxuechao
 * @date 2019-02-15
 */
@Slf4j
@Configuration
public class Consumer {

    private String consumerGroup = "rmq-group";
    private String namesrvAddr = "192.168.212.75:9876;192.168.212.76:9876";
    private String instanceName = "consumer_fxc";
    private String topic = "Topic_A";
    private String subExpression = "TagA||TagB||TagC||TagD||TagE";

    @Bean
    public DefaultMQPushConsumer defaultMQConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);

        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setInstanceName(instanceName);
        try {
            consumer.subscribe(topic, subExpression);
            // 设置Consumer第一次启动是消费的起始点
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

            consumer.registerMessageListener((MessageListenerConcurrently) (msgList, context) -> {
                for (MessageExt msg : msgList) {
                    System.out.println("收到 => " + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
            if (log.isInfoEnabled()) {
                log.info("DefaultMQPushConsumer Started.");
            }
            return consumer;
        } catch (MQClientException e) {
            log.error("DefaultMQPushConsumer启动失败");
        }
        return null;
    }
}
