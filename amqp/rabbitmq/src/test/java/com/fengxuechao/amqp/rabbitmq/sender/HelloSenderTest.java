package com.fengxuechao.amqp.rabbitmq.sender;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/9/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloSenderTest {

    @Autowired
    private HelloSender sender;

    @Test
    public void send() {
        for (int i=0;i<10;i++){
            sender.send(i);
        }
    }
}