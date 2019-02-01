package com.littlefxc.examples;

import com.littlefxc.examples.spring.boot.autoconfigure.SchedulerFactoryBeanCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author fengxuechao
 * @date 2019/1/3
 **/
@Slf4j
@SpringBootApplication
public class TestAutoConfigure {

    public static void main(String[] args) {
        SpringApplication.run(TestAutoConfigure.class, args);
    }

    /**
     * 自定义调度器
     *
     * @return
     */
    @Bean
    public SchedulerFactoryBeanCustomizer dataSourceCustomizer() {
        return (schedulerFactoryBean) -> {
            schedulerFactoryBean.setOverwriteExistingJobs(false);
            schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
            schedulerFactoryBean.setStartupDelay(10);
        };
    }

    /**
     * JobDetailFactoryBean
     *
     * @return
     */
    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setName("job-1");
        bean.setGroup("job-group-1");
        bean.setJobClass(MyJob.class);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("hello", "world");
        bean.setJobDataMap(jobDataMap);
        bean.setDurability(true);
        return bean;
    }

    /**
     * CronTriggerFactoryBean
     *
     * @param jobDetail
     * @return
     */
    @Bean
    public CronTriggerFactoryBean cronTrigger(JobDetail jobDetail) {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setName("cron-1");
        bean.setGroup("cron-group-1");
        bean.setCronExpression("0/5 * * * * ?");
        bean.setJobDetail(jobDetail);
        return bean;
    }

    /**
     * Job
     */
    @Component
    public static class MyJob extends QuartzJobBean {

        @Override
        protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            String jobName = jobDetail.getKey().getName();
            String jobGroup = jobDetail.getKey().getGroup();
            String jobDataMapHello = (String) jobDetail.getJobDataMap().get("hello");
            log.info("job.name = {}, job.group = {}, job.dataMap.hello = {}", jobName, jobGroup, jobDataMapHello);
        }
    }
}
