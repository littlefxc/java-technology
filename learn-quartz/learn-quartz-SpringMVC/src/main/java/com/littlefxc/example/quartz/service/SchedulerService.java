package com.littlefxc.example.quartz.service;


import com.github.pagehelper.PageInfo;
import com.littlefxc.example.quartz.enitiy.SchedulerJob;

import java.util.Map;

/**
 * @author fengxuechao
 * @date 12/19/2018
 */
public interface SchedulerService {

    void startAllSchedulers();

    void scheduleNewJob(SchedulerJob jobInfo);

    void updateScheduleJob(SchedulerJob jobInfo);

    boolean unScheduleJob(String jobName);

    boolean deleteJob(String jobName, String jobGroup);

    boolean pauseJob(String jobName, String jobGroup);

    boolean resumeJob(String jobName, String jobGroup);

    boolean startJobNow(String jobName, String jobGroup);

    PageInfo<Map<String, Object>> findAll(Boolean cron, Integer pageNum, Integer pageSize);

    SchedulerJob findOne(String jobName);
}
