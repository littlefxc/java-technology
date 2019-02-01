package com.littlefxc.examples.redis;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author fengxuechao
 * @date 2019/1/11
 **/
public class RedisExample {

    public static void main(String[] args) throws InterruptedException {
        // jedis 单机配置
//        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:spring-jedis.xml");
        // jedis 集群配置
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:spring-jedis-cluster.xml");

        RedisTemplate<String, String> redisTemplate = (RedisTemplate) beanFactory.getBean("redisTemplate");
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("jedis", "Jedis 配置", 5, TimeUnit.SECONDS);
        String jedis = ops.get("jedis");
        System.out.println(jedis);

        Thread.sleep(5000);

        Boolean hasKey = redisTemplate.hasKey("jedis");
        System.out.println("已经5秒过去了，jedis 还在吗？ "+ hasKey);

        System.exit(0);
    }
}
