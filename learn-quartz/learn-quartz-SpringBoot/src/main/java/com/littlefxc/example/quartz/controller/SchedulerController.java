package com.littlefxc.example.quartz.controller;

import com.littlefxc.example.quartz.enitiy.SchedulerJob;
import com.littlefxc.example.quartz.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengxuechao
 * @date 12/20/2018
 **/
@RestController
@RequestMapping("/job-info")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    /**
     * 根据jobName查询
     * @param jobName
     * @return {@link SchedulerJob}
     */
    @GetMapping("/findOne")
    public SchedulerJob findOne(@RequestParam String jobName) {
        return schedulerService.findOne(jobName);
    }
}
