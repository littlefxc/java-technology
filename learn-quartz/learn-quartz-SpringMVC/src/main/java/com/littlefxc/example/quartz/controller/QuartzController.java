package com.littlefxc.example.quartz.controller;

import com.github.pagehelper.PageInfo;
import com.littlefxc.example.quartz.enitiy.SchedulerJob;
import com.littlefxc.example.quartz.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @param pageNum  default 1
     * @param pageSize default 10
     * @param cron     true:cron trigger, false:simple trigger
     * @return
     */
    @GetMapping(value = "/queryjob")
    public PageInfo<Map<String, Object>> queryjob(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Boolean cron) {
        return schedulerService.findAll(cron, pageNum, pageSize);
    }
}