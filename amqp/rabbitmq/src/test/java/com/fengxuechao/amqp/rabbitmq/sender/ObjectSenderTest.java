package com.fengxuechao.amqp.rabbitmq.sender;

import com.fengxuechao.amqp.rabbitmq.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/9/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ObjectSenderTest {

    @Autowired
    ObjectSender objectSender;

    @Test
    public void send() {
        objectSender.send(new User("user1", "pass1"));
    }
}