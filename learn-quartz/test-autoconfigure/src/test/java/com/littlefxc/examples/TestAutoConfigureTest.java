package com.littlefxc.examples;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author fengxuechao
 * @date 2019/1/3
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestAutoConfigureTest {

    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    @Test
    public void testSchedulerFactoryBean() {
        Assert.assertNotNull(schedulerFactoryBean);
    }

}