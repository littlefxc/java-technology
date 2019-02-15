package com.littlefxc.examples.rocketmq.web;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author fengxuechao
 * @date 2019-02-15
 */
@RestController
public class ProducerController {

    @Autowired
    private DefaultMQProducer producer;

    @RequestMapping("/send")
    public String send(String msg) {
        SendResult result = null;
        try {
            result = producer.send(new Message("Topic_A", msg.getBytes(StandardCharsets.UTF_8)));
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            e.printStackTrace();
        }
        if (result == null) {
            return null;
        }
        return "发送响应：" + result.getMsgId() + ", 发送状态: " + result.getSendStatus();
    }
}
