package com.littlefxc.example.quartz.service.impl;

import com.littlefxc.example.quartz.enitiy.SchedulerJob;
import com.littlefxc.example.quartz.jobs.SampleCronJob;
import com.littlefxc.example.quartz.jobs.SimpleJob;
import com.littlefxc.example.quartz.service.SchedulerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author fengxuechao
 * @date 12/19/2018
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerServiceImplTest {

    @Autowired
    SchedulerService schedulerService;

    SchedulerJob jobInfo;

    boolean cron = false;

    @Before
    public void setUp() {
        jobInfo = new SchedulerJob();
        if (cron) {
            jobInfo.setJobName("sampleCronJob");
            jobInfo.setJobClass(SampleCronJob.class.getName());
            jobInfo.setJobGroup("group2");
            jobInfo.setCronJob(true);
            jobInfo.setCronExpression("*/5 * * ? * *");
            jobInfo.setRepeatTime(3L);
        } else {
            jobInfo.setJobName("simpleJob");
            jobInfo.setJobClass(SimpleJob.class.getName());
            jobInfo.setJobGroup("group1");
            jobInfo.setCronJob(false);
            jobInfo.setCronExpression("*/5 * * ? * *");
            jobInfo.setRepeatTime(3L);
        }


    }

    @Test
    public void scheduleNewJob() {
        schedulerService.scheduleNewJob(jobInfo);
    }

    @Test
    public void updateScheduleJob() {
        if (cron) {
            jobInfo.setId(1L);
            // 设置是不是Cron trigger
            jobInfo.setCronJob(true);
            jobInfo.setCronExpression("*/5 * * ? * *");
            schedulerService.updateScheduleJob(jobInfo);
        } else {
            jobInfo.setId(2L);
            // 设置是不是Cron trigger
            jobInfo.setCronJob(false);
            jobInfo.setRepeatTime(5L);
            jobInfo.setCronExpression(null);
            schedulerService.updateScheduleJob(jobInfo);
        }

    }

    @Test
    public void pauseJob() {
        if (cron) {
            jobInfo.setId(1L);
        } else {
            jobInfo.setId(2L);
        }
        schedulerService.pauseJob(jobInfo.getJobName(), jobInfo.getJobGroup());
    }
}