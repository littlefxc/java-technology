package com.littlefxc.example.quartz.controller;

import com.littlefxc.example.quartz.enitiy.SchedulerJob;
import com.littlefxc.example.quartz.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author fengxuechao
 * @date 12/19/2018
 **/
@RestController
@RequestMapping("/job")
@Slf4j
public class QuartzController {

    private final SchedulerService schedulerService;

    @Autowired
    public QuartzController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    /**
     * 添加
     *
     * @param jobInfo
     */
    @PostMapping(value = "/addjob")
    public void addjob(@RequestBody SchedulerJob jobInfo) {
        schedulerService.scheduleNewJob(jobInfo);
    }

    /**
     * 暂停
     *
     * @param jobName
     * @param jobGroup
     */
    @PostMapping(value = "/pausejob")
    public void pausejob(
            @RequestParam String jobName, @RequestParam String jobGroup) {
        schedulerService.pauseJob(jobName, jobGroup);
    }

    /**
     * 恢复启动
     *
     * @param jobName
     * @param jobGroup
     */
    @PostMapping(value = "/resumejob")
    public void resumejob(@RequestParam String jobName, @RequestParam String jobGroup) {
        schedulerService.resumeJob(jobName, jobGroup);
    }

    /**
     * 更新：移除older trigger,添加new trigger
     *
     * @param jobInfo
     */
    @PostMapping(value = "/reschedulejob")
    public void rescheduleJob(@RequestBody SchedulerJob jobInfo) {
        schedulerService.updateScheduleJob(jobInfo);
    }

    /**
     * 删除
     *
     * @param jobName
     * @param jobGroup
     */
    @PostMapping(value = "/deletejob")
    public void deletejob(@RequestParam String jobName, @RequestParam String jobGroup) {
        schedulerService.deleteJob(jobName, jobGroup);
    }

    /**
     * 查询
     *
     * @param pageable
     * @param cron
     * @return
     */
    @GetMapping(value = "/queryjob")
    public Page<Map<String, Object>> queryjob(
            @PageableDefault Pageable pageable, @RequestParam Boolean cron) {
        return schedulerService.findAll(pageable, cron);
    }
}