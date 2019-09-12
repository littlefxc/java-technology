package com.fengxuechao.amqp.rabbitmq.receiver;

import com.fengxuechao.amqp.rabbitmq.bean.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/9/12
 */
@Component
@RabbitListener(queues = "object")
public class ObjectReceiver {

    @RabbitHandler
    public void process(User user) {
        System.out.println("Receiver object : " + user);
    }

}

