package com.littlefxc.example.quartz.service;


import com.littlefxc.example.quartz.enitiy.SchedulerJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<Map<String, Object>> findAll(Pageable pageable, Boolean cron);

    SchedulerJob findOne(String jobName);
}
