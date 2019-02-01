package com.littlefxc.example.quartz.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author fengxuechao
 * @date 12/19/2018
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerRepositoryTest {

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Test
    public void getJobAndTriggerDetails() throws SchedulerException {
        Page<Map<String, Object>> jobAndTriggerDetails = schedulerRepository
                .getJobWithSimpleTrigger(
                        schedulerFactoryBean.getScheduler().getSchedulerName(),
                        PageRequest.of(10, 10));
        System.out.println("totalElements : " + jobAndTriggerDetails.getTotalElements());
        System.out.println("totalPages : " + jobAndTriggerDetails.getTotalPages());
    }
}