package com.littlefxc.example.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.stream.IntStream;

/**
 * @author fengxuechao
 * @date 12/19/2018
 */
@Slf4j
@DisallowConcurrentExecution // 这个注解告诉Quartz，一个给定的Job定义（也就是一个JobDetail实例），不并发运行。
public class SampleCronJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("{} Start................", context.getJobDetail().getKey());
        IntStream.range(0, 10).forEach(i -> {
            log.info("Counting - {}", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });
        log.info("{} End................", context.getJobDetail().getKey());
    }
}
