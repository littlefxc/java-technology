package com.littlefxc.example.quartz.enitiy;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.Serializable;

/**
 * @author fengxuechao
 * @date 12/19/2018
 */
@Data
@Alias("SchedulerJob")
public class SchedulerJob implements Serializable {

    private static final long serialVersionUID = -8990533448070839127L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 任务名
     */
    private String jobName;

    /**
     * 任务所在组
     */
    private String jobGroup;

    /**
     * 任务类名
     */
    private String jobClass;

    /**
     * cron 表达式
     */
    private String cronExpression;

    /**
     * 触发间隔(毫秒)
     */
    private Long repeatTime;

    /**
     * 是不是Cron Trigger
     */
    private Boolean cronJob;

    /**
     * 唯一
     * {@link SchedulerFactoryBean#schedulerName}
     */
    private String schedulerName;
}