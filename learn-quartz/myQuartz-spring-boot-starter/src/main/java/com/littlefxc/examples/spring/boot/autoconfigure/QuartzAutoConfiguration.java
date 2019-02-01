package com.littlefxc.examples.spring.boot.autoconfigure;

import org.quartz.Calendar;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author fengxuechao
 * @date 2019/1/2
 **/
@Configuration
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class, PlatformTransactionManager.class})
@EnableConfigurationProperties(QuartzProperties.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class QuartzAutoConfiguration {

    private final static Logger log = LoggerFactory.getLogger(QuartzAutoConfiguration.class);

    private final List<SchedulerFactoryBeanCustomizer> customizers;

    private final QuartzProperties properties;

    private final JobDetail[] jobDetails;

    private final Map<String, Calendar> calendars;

    private final Trigger[] triggers;

    private final ApplicationContext applicationContext;

    private final DataSource dataSource;

    private final PlatformTransactionManager transactionManager;

    /**
     * <p>
     * ObjectProvider:是现有ObjectFactory接口的扩展,具有方便的签名，
     * 例如getIfAvailable和getIfUnique，
     * 只有在它实际存在时才检索bean（可选支持）或者如果可以确定单个候选者（特别是：主要候选者）在多个匹配的bean的情况下）
     * </p>
     */
    public QuartzAutoConfiguration(QuartzProperties properties,
                                   ObjectProvider<List<SchedulerFactoryBeanCustomizer>> customizers,
                                   ObjectProvider<JobDetail[]> jobDetails,
                                   ObjectProvider<Map<String, Calendar>> calendars,
                                   ObjectProvider<Trigger[]> triggers,
                                   ApplicationContext applicationContext,
                                   ObjectProvider<DataSource> dataSource,
                                   ObjectProvider<PlatformTransactionManager> transactionManager) {
        this.properties = properties;
        this.jobDetails = jobDetails.getIfAvailable();
        this.calendars = calendars.getIfAvailable();
        this.triggers = triggers.getIfAvailable();
        this.applicationContext = applicationContext;
        this.dataSource = dataSource.getIfAvailable();
        this.transactionManager = transactionManager.getIfAvailable();
        this.customizers = customizers.getIfAvailable();
    }

    /**
     * 如果没有调度器，就创建
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public SchedulerFactoryBean schedulerFactoryBean() {
        log.info("Init SchedulerFactoryBean");
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(new AutoSchedulerJobFactory(this.applicationContext.getAutowireCapableBeanFactory()));
        if (!this.properties.getProperties().isEmpty()) {
            schedulerFactoryBean.setQuartzProperties(asProperties(this.properties.getProperties()));
        }
        if (this.jobDetails != null && this.jobDetails.length > 0) {
            schedulerFactoryBean.setJobDetails(this.jobDetails);
        }
        if (this.calendars != null && !this.calendars.isEmpty()) {
            schedulerFactoryBean.setCalendars(this.calendars);
        }
        if (this.triggers != null && this.triggers.length > 0) {
            schedulerFactoryBean.setTriggers(this.triggers);
        }
        // todo quartz数据源初始化
        if (properties.getJobStoreType() == JobStoreType.JDBC) {
            if (dataSource != null) {
                schedulerFactoryBean.setDataSource(dataSource);
            }
            if (transactionManager != null) {
                schedulerFactoryBean.setTransactionManager(transactionManager);
            }
        }
        // 自定义调度器工厂
        customize(schedulerFactoryBean);
        return schedulerFactoryBean;
    }

    /**
     * 自定义调度器
     *
     * @param schedulerFactoryBean
     */
    private void customize(SchedulerFactoryBean schedulerFactoryBean) {
        if (this.customizers != null) {
            for (SchedulerFactoryBeanCustomizer customizer : this.customizers) {
                customizer.customize(schedulerFactoryBean);
            }
        }
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }

}
