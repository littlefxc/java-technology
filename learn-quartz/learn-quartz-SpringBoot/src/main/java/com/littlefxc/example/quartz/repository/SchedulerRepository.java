package com.littlefxc.example.quartz.repository;

import com.littlefxc.example.quartz.enitiy.SchedulerJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author fengxuechao
 * @date 12/19/2018
 */
@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJob, Long> {

    /**
     * 仅查询simple trigger关联的Job
     * 不查询cron trigger关联的job
     *
     * @param schedulerName
     * @param pageable      分页信息
     * @return
     */
    @Query(value = "select " +
            "j.JOB_NAME, " +
            "j.JOB_GROUP, " +
            "j.JOB_CLASS_NAME, " +
            "t.TRIGGER_NAME, " +
            "t.TRIGGER_GROUP, " +
            "s.REPEAT_INTERVAL, " +
            "s.TIMES_TRIGGERED " +
            "from qrtz_job_details as j " +
            "join qrtz_triggers as t " +
            "join qrtz_simple_triggers as s ON j.JOB_NAME = t.JOB_NAME " +
            "and t.TRIGGER_NAME = s.TRIGGER_NAME " +
            "and t.TRIGGER_GROUP = s.TRIGGER_GROUP " +
            "where j.SCHED_NAME = ?1 " +
            "order by ?#{#pageable}",
            countQuery = "select count(1) " +
                    "from qrtz_job_details as j " +
                    "join qrtz_triggers as t " +
                    "join qrtz_cron_triggers as c  ON j.JOB_NAME = t.JOB_NAME " +
                    "and t.TRIGGER_NAME = c.TRIGGER_NAME " +
                    "and t.TRIGGER_GROUP = c.TRIGGER_GROUP " +
                    "where j.SCHED_NAME = ?1",
            nativeQuery = true)
    Page<Map<String, Object>> getJobWithSimpleTrigger(String schedulerName, Pageable pageable);

    /**
     * 仅查询cron trigger关联的Job
     * 不查询simple trigger关联的job
     *
     * @param schedulerName
     * @param pageable
     * @return
     */
    @Query(value = "select " +
            "j.JOB_NAME, " +
            "j.JOB_GROUP, " +
            "j.JOB_CLASS_NAME, " +
            "t.TRIGGER_NAME, " +
            "t.TRIGGER_GROUP, " +
            "c.CRON_EXPRESSION, " +
            "c.TIME_ZONE_ID " +
            "from qrtz_job_details as j " +
            "join qrtz_triggers as t " +
            "join qrtz_cron_triggers as c ON j.JOB_NAME = t.JOB_NAME " +
            "and t.TRIGGER_NAME = c.TRIGGER_NAME " +
            "and t.TRIGGER_GROUP = c.TRIGGER_GROUP " +
            "where j.SCHED_NAME = ?1 " +
            "order by ?#{#pageable}",
            countQuery = "select count(1) " +
                    "from qrtz_job_details as j " +
                    "join qrtz_triggers as t " +
                    "join qrtz_cron_triggers as c  ON j.JOB_NAME = t.JOB_NAME " +
                    "and t.TRIGGER_NAME = c.TRIGGER_NAME " +
                    "and t.TRIGGER_GROUP = c.TRIGGER_GROUP " +
                    "where j.SCHED_NAME = ?1 ",
            nativeQuery = true)
    Page<Map<String, Object>> getJobWithCronTrigger(String schedulerName, Pageable pageable);

    /**
     * 根据JobName查询SchedulerJob
     *
     * @param schedulerName
     * @param jobName
     * @return SchedulerJob
     */
    @Query("select s from SchedulerJob s where s.schedulerName=?1 and s.jobName=?2")
    SchedulerJob findSchedulerJob(String schedulerName, String jobName);
}
