package com.littlefxc.examples.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author fengxuechao
 * @date 2019-02-15
 */
@Component
public class PullConsumer {

    private static final Map<MessageQueue, Long> offsetTable = new HashMap<MessageQueue, Long>();

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        offsetTable.put(mq, offset);
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = offsetTable.get(mq);
        if (offset != null) {
            return offset;
        }
        return 0;
    }

    @Bean
    @ConditionalOnClass({DefaultMQPullConsumer.class})
    @ConditionalOnMissingBean({DefaultMQPullConsumer.class})
    public DefaultMQPullConsumer mqPullConsumer() throws MQClientException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("rmq-group");
        consumer.setNamesrvAddr("192.168.212.75:9876;192.168.212.76:9876");
        return consumer;
    }

    @EventListener(condition = "#event!=null")
    public void pullConsumerEventListener(PullConsumerEvent event) {
        System.err.println(event.getName());
        try {
            DefaultMQPullConsumer consumer = mqPullConsumer();
            Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("Topic_A");
            for (MessageQueue mq : mqs) {
                System.err.println("Consume from the queue: " + mq);
                SINGLE_MQ:
                while (true) {
                    try {
                        PullResult pullResult = consumer.pullBlockIfNotFound(mq, "TagA", getMessageQueueOffset(mq), 32);
                        System.out.println(pullResult);
                        putMessageQueueOffset(mq, pullResult.getNextBeginOffset());

                        switch (pullResult.getPullStatus()) {
                            case FOUND:
                                List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                                for (MessageExt m : messageExtList) {
                                    System.out.println(new String(m.getBody()));
                                }
                                break;
                            case NO_MATCHED_MSG:
                                break;
                            case NO_NEW_MSG:
                                break SINGLE_MQ;
                            case OFFSET_ILLEGAL:
                                break;
                            default:
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

}
