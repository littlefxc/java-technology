package com.littlefxc.example.quartz.dao;

import com.github.pagehelper.PageInfo;
import com.littlefxc.example.quartz.enitiy.SchedulerJob;
import com.littlefxc.example.quartz.jobs.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author fengxuechao
 * @date 12/19/2018
 **/
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
@Slf4j
@Transactional
public class SchedulerDaoTest {

    @Autowired
    private SchedulerDao schedulerDao;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Test
    public void getJobWithSimpleTrigger() throws SchedulerException {
        List<Map<String, Object>> list = schedulerDao.getJobWithSimpleTrigger(schedulerFactoryBean.getScheduler().getSchedulerName(), new RowBounds(0, 10));
        list.forEach(map -> map.forEach((key, val) -> log.debug("key = {}, value = {}", key, val)));
        PageInfo<Map<String, Object>> pageInfo = PageInfo.of(list);
        Assert.assertNotEquals(-1, pageInfo.getTotal());
    }

    @Test
    public void getJobWithCronTrigger() throws SchedulerException {
        List<Map<String, Object>> list = schedulerDao.getJobWithCronTrigger(schedulerFactoryBean.getScheduler().getSchedulerName(), new RowBounds(0, 10));
        list.forEach(map -> map.forEach((key, val) -> log.debug("key = {}, value = {}", key, val)));
    }

    @Test
    public void findSchedulerJobByJobName() throws SchedulerException {
        String jobName = "SimpleJob";
        SchedulerJob job = schedulerDao.findSchedulerJobByJobName(schedulerFactoryBean.getScheduler().getSchedulerName(), jobName);
        Assert.assertEquals(jobName, job.getJobName());
    }

    @Test
    public void findAll() throws SchedulerException {
        schedulerDao.findAll(schedulerFactoryBean.getScheduler().getSchedulerName());
    }

    @Test
    public void save() {
        SchedulerJob jobInfo = new SchedulerJob();
        jobInfo.setJobName("simpleJob_1");
        jobInfo.setJobClass(SimpleJob.class.getName());
        jobInfo.setJobGroup("group1");
        jobInfo.setCronJob(false);
        jobInfo.setCronExpression("*/5 * * ? * *");
        jobInfo.setRepeatTime(3L);
        Integer save = schedulerDao.save(jobInfo);
        Assert.assertEquals(1, save.intValue());
        log.debug(jobInfo.toString());
    }

    @Test
    public void deleteByJobName() throws SchedulerException {
        Integer delete = schedulerDao.deleteByJobName("simpleJob", schedulerFactoryBean.getScheduler().getSchedulerName());
        Assert.assertEquals(1, delete.intValue());
    }
}