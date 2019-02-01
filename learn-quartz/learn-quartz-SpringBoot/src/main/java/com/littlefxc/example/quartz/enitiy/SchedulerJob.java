package com.littlefxc.example.quartz.enitiy;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author fengxuechao
 * @date 12/19/2018
 */
@Data
@Entity
@Table(name = "scheduler_job_info")
public class SchedulerJob implements Serializable {

    private static final long serialVersionUID = -8990533448070839127L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String jobName;

    private String jobGroup;

    private String jobClass;

    private String cronExpression;

    private Long repeatTime;

    private Boolean cronJob;

    private String schedulerName;
}