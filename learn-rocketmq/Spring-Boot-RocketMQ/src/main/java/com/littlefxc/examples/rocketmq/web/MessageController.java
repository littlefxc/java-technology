package com.littlefxc.examples.rocketmq.web;

import com.littlefxc.examples.rocketmq.consumer.PullConsumerEvent;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengxuechao
 * @date 2019-02-15
 */
@RestController
public class MessageController {

    @Autowired
    private ApplicationContext publisher;

    @Autowired
    private DefaultMQProducer producer;

    /**
     * 发送同步消息
     *
     * @throws Exception
     */
    @RequestMapping("send")
    public void send() throws Exception {
        for (int i = 0; i < 10; i++) {
            Message message = new Message(
                    "Topic_A",
                    "TagA",
                    ("同步消息 " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            producer.send(message);
        }
        publisher.publishEvent(new PullConsumerEvent("pullConsumerEvent"));
    }
}
