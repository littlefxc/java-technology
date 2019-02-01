package com.littlefxc.example.quartz.dao;

import com.littlefxc.example.quartz.enitiy.SchedulerJob;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author fengxuechao
 * @date 12/19/2018
 */
@Repository
public interface SchedulerDao {

    /**
     * 仅查询simple trigger关联的Job
     * 不查询cron trigger关联的job
     *
     * @param schedulerName
     * @param rowBounds     分页插件
     * @return
     */
    List<Map<String, Object>> getJobWithSimpleTrigger(String schedulerName, RowBounds rowBounds);

    /**
     * 仅查询cron trigger关联的Job
     * 不查询simple trigger关联的job
     *
     * @param schedulerName
     * @param rowBounds     分页插件
     * @return
     */
    List<Map<String, Object>> getJobWithCronTrigger(String schedulerName, RowBounds rowBounds);

    /**
     * 根据JobName查询SchedulerJob
     *
     * @param schedulerName
     * @param jobName
     * @return SchedulerJob
     */
    SchedulerJob findSchedulerJobByJobName(String schedulerName, String jobName);

    /**
     * 列表查询Job
     *
     * @param schedulerName
     * @return
     */
    List<SchedulerJob> findAll(String schedulerName);

    /**
     * 保存和更新
     * 当id==null时，更新
     *
     * @param jobInfo
     * @return
     */
    Integer save(SchedulerJob jobInfo);

    /**
     * 删除Job
     *
     * @param jobName
     * @param schedulerName
     * @return
     */
    Integer deleteByJobName(String jobName, String schedulerName);
}
