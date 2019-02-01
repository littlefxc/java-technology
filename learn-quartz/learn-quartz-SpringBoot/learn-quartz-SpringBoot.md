
# 学习 Spring Boot 下的quartz 动态任务调度 

# 2.Maven

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<project xmlns="http://maven.apache.org/POM/4.0.0"  
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">  
    <modelVersion>4.0.0</modelVersion>  
  
    <groupId>com.littlefxc</groupId>  
    <artifactId>learn-quartz-SpringBoot</artifactId>  
    <version>1.0-snapshot</version>  
  
    <properties>  
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>  
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>  
        <java.version>1.8</java.version>  
    </properties>  
  
    <parent>  
        <groupId>org.springframework.boot</groupId>  
        <artifactId>spring-boot-starter-parent</artifactId>  
        <version>2.0.4.RELEASE</version>  
    </parent>  
  
    <dependencies>  
        <dependency>  
            <groupId>org.projectlombok</groupId>  
            <artifactId>lombok</artifactId>  
        </dependency>  
        <dependency>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-web</artifactId>  
        </dependency>  
        <dependency>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-quartz</artifactId>  
        </dependency>  
        <dependency>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-data-jpa</artifactId>  
        </dependency>  
        <dependency>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-test</artifactId>  
            <scope>test</scope>  
        </dependency>  
        <dependency>  
            <groupId>com.alibaba</groupId>  
            <artifactId>druid-spring-boot-starter</artifactId>  
            <version>1.1.10</version>  
        </dependency>  
        <dependency>  
            <groupId>mysql</groupId>  
            <artifactId>mysql-connector-java</artifactId>  
        </dependency>  
        <dependency>  
            <groupId>javax.servlet</groupId>  
            <artifactId>javax.servlet-api</artifactId>  
            <version>3.1.0</version>  
            <scope>provided</scope>  
        </dependency>  
    </dependencies>  
</project>  
```

# 3.数据库-模型

在jar包quartz-2.3.0.jar下有数据库sql文件.

sql文件的包路径地址：org.quartz.impl.jdbcjobstore，选择tables_mysql_innodb.sql

## 3.1. scheduler_job_info.sql

```sql
DROP TABLE IF EXISTS `scheduler_job_info`;  
CREATE TABLE `scheduler_job_info`  (  
  `id` bigint(20) NOT NULL AUTO_INCREMENT,  
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,  
  `cron_job` bit(1) NULL DEFAULT NULL,  
  `job_class` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,  
  `job_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,  
  `job_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,  
  `scheduler_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,  
  `repeat_time` bigint(20) NULL DEFAULT NULL,  
  PRIMARY KEY (`id`) USING BTREE,  
  UNIQUE INDEX `uk_job_name`(`job_name`) USING BTREE  
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;  
  
SET FOREIGN_KEY_CHECKS = 1;  
```

## 3.2.实体类

```java
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
```

# 4.配置

## 4.1.application.properties

`com.littlefxc.example.quartz.component.CustomQuartzInstanceIdGenerator`表示使用自定义的实例名生成策略，该类代码可以在5.1章节中看到,在数据库上的代码实际效果可以查看到(表qrtz_scheduler_state, 字段INSTANCE_NAME)。

```properties
spring.application.name=learn-quartz-SpringBoot  
  
# jackson Config  
spring.jackson.time-zone=GMT+8  
spring.jackson.date-format=yyyy-MM-dd HH:mm:sss  
#spring.jackson.property-naming-strategy=SNAKE_CASE  
  
# DataSource Config  
spring.datasource.url=jdbc:mysql://localhost:3306/learn-quartz?useSSL=false  
spring.datasource.username=root  
spring.datasource.password=123456  
spring.datasource.driver-class-name=com.mysql.jdbc.Driver  
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource  
spring.datasource.druid.filters=slf4j,wall  
spring.datasource.druid.initial-size=1  
spring.datasource.druid.min-idle=1  
spring.datasource.druid.max-active=8  
spring.datasource.druid.max-wait=60000  
spring.datasource.druid.time-between-eviction-runs-millis=60000  
spring.datasource.druid.min-evictable-idle-time-millis=300000  
spring.datasource.druid.test-while-idle=true  
spring.datasource.druid.test-on-borrow=false  
spring.datasource.druid.test-on-return=false  
spring.datasource.druid.pool-prepared-statements=true  
spring.datasource.druid.max-pool-prepared-statement-per-connection-size=20  
  
# JPA Config  
spring.jpa.hibernate.ddl-auto=update  
#spring.jpa.open-in-view=false  
spring.jpa.show-sql=true  
  
# Quartz Config  
spring.quartz.job-store-type=jdbc  
spring.quartz.jdbc.initialize-schema=never  
  
spring.quartz.properties.org.quartz.scheduler.instanceName=${spring.application.name}  
spring.quartz.properties.org.quartz.scheduler.instanceId=AUTO  
spring.quartz.properties.org.quartz.scheduler.instanceIdGenerator.class=com.littlefxc.example.quartz.component.CustomQuartzInstanceIdGenerator  
spring.quartz.properties.org.quartz.threadPool.threadCount=20  
spring.quartz.properties.org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX  
spring.quartz.properties.org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate  
spring.quartz.properties.org.quartz.jobStore.useProperties=true  
spring.quartz.properties.org.quartz.jobStore.misfireThreshold=60000  
spring.quartz.properties.org.quartz.jobStore.tablePrefix=qrtz_  
spring.quartz.properties.org.quartz.jobStore.isClustered=true  
spring.quartz.properties.org.quartz.plugin.shutdownHook.class=org.quartz.plugins.management.ShutdownHookPlugin  
spring.quartz.properties.org.quartz.plugin.shutdownHook.cleanShutdown=TRUE  
```

## 4.2.自定义SchedulerFactoryBean

创建SchedulerFactoryBean。
黄色代码高亮处表示在SchedulerFactoryBean中注入Spring上下文(applicationContext)，该类(SchedulerJobFactory)可以在5.2章节中详细查看

```java
package com.littlefxc.example.quartz.config;  
  
import com.littlefxc.example.quartz.component.SchedulerJobFactory;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;  
import org.springframework.context.ApplicationContext;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
import org.springframework.scheduling.quartz.SchedulerFactoryBean;  
  
import javax.sql.DataSource;  
import java.util.Properties;  
  
/** 
 * @author fengxuechao 
 * @date 12/19/2018 
 */  
@Configuration  
public class SchedulerConfig {  
  
    @Autowired  
    private DataSource dataSource;  
  
    @Autowired  
    private ApplicationContext applicationContext;  
  
    @Autowired  
    private QuartzProperties quartzProperties;  
  
    /**
     * create scheduler factory 
     */  
    @Bean  
    public SchedulerFactoryBean schedulerFactoryBean() {  
  
        SchedulerJobFactory jobFactory = new SchedulerJobFactory();  
        jobFactory.setApplicationContext(applicationContext);  
  
        Properties properties = new Properties();  
        properties.putAll(quartzProperties.getProperties());  
  
        SchedulerFactoryBean factory = new SchedulerFactoryBean();  
        factory.setOverwriteExistingJobs(true);  
        factory.setDataSource(dataSource);  
        factory.setQuartzProperties(properties);  
        factory.setJobFactory(jobFactory);  
        return factory;  
    }  
  
}  
```

# 5.组件

## 5.1.CustomQuartzInstanceIdGenerator

用法详见4.1章节

```java
package com.littlefxc.example.quartz.component;  
  
import org.quartz.SchedulerException;  
import org.quartz.spi.InstanceIdGenerator;  
  
import java.util.UUID;  
  
/** 
 * @author fengxuechao 
 * @date 12/19/2018 
 */  
public class CustomQuartzInstanceIdGenerator implements InstanceIdGenerator {  
  
    @Override  
    public String generateInstanceId() throws SchedulerException {  
        try {  
            return UUID.randomUUID().toString();  
        } catch (Exception ex) {  
            throw new SchedulerException("Couldn't generate UUID!", ex);  
        }  
    }  
  
}
```  

## 5.2.SchedulerJobFactory

Quartz与Spring结合。
在SchedulerFactory中引入Spring上下文。
用法详见4.2章节。

```java
package com.littlefxc.example.quartz.component;  
  
import org.quartz.spi.TriggerFiredBundle;  
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;  
import org.springframework.context.ApplicationContext;  
import org.springframework.context.ApplicationContextAware;  
import org.springframework.scheduling.quartz.SpringBeanJobFactory;  
  
/** 
 * 模仿了：{@link org.springframework.boot.autoconfigure.quartz.AutowireCapableBeanJobFactory} 
 * 
 * @author fengxuechao 
 * @date 12/19/2018 
 * @see <a href="http://blog.btmatthews.com/?p=40#comment-33797">注入Spring上下文(applicationContext) 
 */  
public class SchedulerJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {  
  
    private AutowireCapableBeanFactory beanFactory;  
  
    @Override  
    public void setApplicationContext(final ApplicationContext context) {  
        beanFactory = context.getAutowireCapableBeanFactory();  
    }  
  
    @Override  
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {  
        final Object job = super.createJobInstance(bundle);  
        beanFactory.autowireBean(job);  
        return job;  
    }  
}  
```

## 5.3.JobScheduleCreator

Scheduler 创建Job，SimpleTrigger，CronTrigger的封装类。
用法在service 层体现。

```java
package com.littlefxc.example.quartz.component;  
  
import lombok.extern.slf4j.Slf4j;  
import org.quartz.CronTrigger;  
import org.quartz.JobDataMap;  
import org.quartz.JobDetail;  
import org.quartz.SimpleTrigger;  
import org.springframework.context.ApplicationContext;  
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;  
import org.springframework.scheduling.quartz.JobDetailFactoryBean;  
import org.springframework.scheduling.quartz.QuartzJobBean;  
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;  
import org.springframework.stereotype.Component;  
  
import java.text.ParseException;  
import java.util.Date;  
  
/** 
 * Scheduler创建Job, SimpleTrigger, CronTrigger 
 * 
 * @author fengxuechao 
 * @date 12/19/2018 
 * @see <a href="https://blog.csdn.net/yangshangwei/article/details/78539433#withmisfirehandlinginstructiondonothing">Quartz-错过触发机制</a> 
 */  
@Slf4j  
@Component  
public class JobScheduleCreator {  
  
    /** 
     * Create Quartz Job. 
     * 
     * @param jobClass  Class whose executeInternal() method needs to be called. 
     * @param isDurable Job needs to be persisted even after completion. if true, job will be persisted, not otherwise. 
     * @param context   Spring application context. 
     * @param jobName   Job name. 
     * @param jobGroup  Job group. 
     * @return JobDetail object 
     */  
    public JobDetail createJob(Class<? extends QuartzJobBean> jobClass, boolean isDurable,  
                               ApplicationContext context, String jobName, String jobGroup) {  
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();  
        factoryBean.setJobClass(jobClass);  
        factoryBean.setDurability(isDurable);  
        factoryBean.setApplicationContext(context);  
        factoryBean.setName(jobName);  
        factoryBean.setGroup(jobGroup);  
  
        // set job data map  
        JobDataMap jobDataMap = new JobDataMap();  
        jobDataMap.put(jobName + jobGroup, jobClass.getName());  
        factoryBean.setJobDataMap(jobDataMap);  
  
        factoryBean.afterPropertiesSet();  
  
        return factoryBean.getObject();  
    }  
  
    /** 
     * Create cron trigger. 
     * 
     * @param triggerName        Trigger name. 
     * @param startTime          Trigger start time. 
     * @param cronExpression     Cron expression. 
     * @param misFireInstruction Misfire instruction (what to do in case of misfire happens). 
     * @return {@link CronTrigger} 
     */  
    public CronTrigger createCronTrigger(String triggerName, Date startTime, String cronExpression, int misFireInstruction) {  
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();  
        factoryBean.setName(triggerName);  
        factoryBean.setStartTime(startTime);  
        factoryBean.setCronExpression(cronExpression);  
        factoryBean.setMisfireInstruction(misFireInstruction);  
        try {  
            factoryBean.afterPropertiesSet();  
        } catch (ParseException e) {  
            log.error(e.getMessage(), e);  
        }  
        return factoryBean.getObject();  
    }  
  
    /** 
     * Create simple trigger. 
     * 
     * @param triggerName        Trigger name. 
     * @param startTime          Trigger start time. 
     * @param repeatTime         Job repeat period mills 
     * @param misFireInstruction Misfire instruction (what to do in case of misfire happens). 
     * @return {@link SimpleTrigger} 
     */  
    public SimpleTrigger createSimpleTrigger(String triggerName, Date startTime, Long repeatTime, int misFireInstruction) {  
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();  
        factoryBean.setName(triggerName);  
        factoryBean.setStartTime(startTime);  
        factoryBean.setRepeatInterval(repeatTime);  
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);  
        factoryBean.setMisfireInstruction(misFireInstruction);  
        factoryBean.afterPropertiesSet();  
        return factoryBean.getObject();  
    }  
}  
```

# 6.Jobs

## Simple Job 

```java
package com.littlefxc.example.quartz.jobs;  
  
import lombok.extern.slf4j.Slf4j;  
import org.quartz.JobExecutionContext;  
import org.quartz.JobExecutionException;  
import org.springframework.scheduling.quartz.QuartzJobBean;  
  
import java.util.stream.IntStream;  
  
/** 
 * @author fengxuechao 
 * @date 12/19/2018 
 */  
@Slf4j  
public class SimpleJob extends QuartzJobBean {  
    @Override  
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {  
        log.info("{} Start................", context.getJobDetail().getKey());  
        IntStream.range(0, 5).forEach(i -> {  
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
```

## Cron Job 

```java
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
```

# 7.控制器层

## 7.1.QuartzController

工作调度的主要代码

```java
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
```

## 7.2.SchedulerController

仅对自定义数据库(scheduler_job_info)操作的控制器。

```java
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
```

# 8.Service层

## 8.1.SchedulerServiceImpl 

```java
package com.littlefxc.example.quartz.service.impl;  
  
import com.littlefxc.example.quartz.component.JobScheduleCreator;  
import com.littlefxc.example.quartz.enitiy.SchedulerJob;  
import com.littlefxc.example.quartz.repository.SchedulerRepository;  
import com.littlefxc.example.quartz.service.SchedulerService;  
import lombok.extern.slf4j.Slf4j;  
import org.quartz.*;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.context.ApplicationContext;  
import org.springframework.data.domain.Page;  
import org.springframework.data.domain.Pageable;  
import org.springframework.scheduling.quartz.QuartzJobBean;  
import org.springframework.scheduling.quartz.SchedulerFactoryBean;  
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;  
  
import java.util.Date;  
import java.util.List;  
import java.util.Map;  
  
/** 
 * @author fengxuechao 
 * @date 12/19/2018 
 */  
@Slf4j  
@Transactional(rollbackFor = Exception.class)  
@Service  
public class SchedulerServiceImpl implements SchedulerService {  
  
    @Autowired  
    private SchedulerFactoryBean schedulerFactoryBean;  
  
    @Autowired  
    private SchedulerRepository schedulerRepository;  
  
    @Autowired  
    private ApplicationContext context;  
  
    @Autowired  
    private JobScheduleCreator scheduleCreator;  
  
    /** 
     * 启动所有的在表scheduler_job_info中记录的job 
     */  
    @Override  
    public void startAllSchedulers() {  
        List<SchedulerJob> jobInfoList = schedulerRepository.findAll();  
        if (jobInfoList != null) {  
            Scheduler scheduler = schedulerFactoryBean.getScheduler();  
            jobInfoList.forEach(jobInfo -> {  
                try {  
                    JobDetail jobDetail = JobBuilder.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))  
                            .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();  
                    if (!scheduler.checkExists(jobDetail.getKey())) {  
                        Trigger trigger;  
                        jobDetail = scheduleCreator.createJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()),  
                                false, context, jobInfo.getJobName(), jobInfo.getJobGroup());  
  
                        if (jobInfo.getCronJob() && CronExpression.isValidExpression(jobInfo.getCronExpression())) {  
                            trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),  
                                    jobInfo.getCronExpression(), CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);  
                        } else {  
                            trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),  
                                    jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);  
                        }  
  
                        scheduler.scheduleJob(jobDetail, trigger);  
  
                    }  
                } catch (ClassNotFoundException e) {  
                    log.error("Class Not Found - {}", jobInfo.getJobClass(), e);  
                } catch (SchedulerException e) {  
                    log.error(e.getMessage(), e);  
                }  
            });  
        }  
    }  
  
    @Override  
    public void scheduleNewJob(SchedulerJob jobInfo) {  
        try {  
            Scheduler scheduler = schedulerFactoryBean.getScheduler();  
  
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))  
                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();  
            if (!scheduler.checkExists(jobDetail.getKey())) {  
  
                jobDetail = scheduleCreator.createJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()),  
                        false, context, jobInfo.getJobName(), jobInfo.getJobGroup());  
  
                Trigger trigger;  
                if (jobInfo.getCronJob()) {  
                    trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(), jobInfo.getCronExpression(),  
                            CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);  
                } else {  
                    trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(),  
                            SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);  
                }  
  
                scheduler.scheduleJob(jobDetail, trigger);  
                jobInfo.setSchedulerName(schedulerFactoryBean.getScheduler().getSchedulerName());  
                schedulerRepository.save(jobInfo);  
            } else {  
                log.error("scheduleNewJobRequest.jobAlreadyExist");  
            }  
        } catch (ClassNotFoundException e) {  
            log.error("Class Not Found - {}", jobInfo.getJobClass(), e);  
        } catch (SchedulerException e) {  
            log.error(e.getMessage(), e);  
        }  
    }  
  
    @Override  
    public void updateScheduleJob(SchedulerJob jobInfo) {  
        Trigger newTrigger;  
        if (jobInfo.getCronJob()) {  
            newTrigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(), jobInfo.getCronExpression(),  
                    CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);  
        } else {  
            newTrigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(),  
                    SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);  
        }  
        try {  
            schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);  
            jobInfo.setSchedulerName(schedulerFactoryBean.getScheduler().getSchedulerName());  
            schedulerRepository.save(jobInfo);  
        } catch (SchedulerException e) {  
            log.error(e.getMessage(), e);  
        }  
    }  
  
    /** 
     * unscheduleJob(TriggerKey triggerKey)只是不再调度触发器，所以，当其他的触发器引用了这个Job，它们不会被改变 
     * 
     * @param jobName 
     * @return 
     */  
    @Override  
    public boolean unScheduleJob(String jobName) {  
        try {  
            return schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName));  
        } catch (SchedulerException e) {  
            log.error("Failed to un-schedule job - {}", jobName, e);  
            return false;  
        }  
    }  
  
    /** 
     * deleteJob(JobKey jobKey):<br> 
     * 1.循环遍历所有引用此Job的触发器，以取消它们的调度(to unschedule them)<br> 
     * 2.从jobstore中删除Job 
     * 
     * @param jobName  job name 
     * @param jobGroup job group 
     * @return 
     */  
    @Override  
    public boolean deleteJob(String jobName, String jobGroup) {  
        try {  
            boolean deleteJob = schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobName, jobGroup));  
            if (deleteJob) {  
                SchedulerJob job = schedulerRepository.findSchedulerJobByJobName(jobName);  
                schedulerRepository.delete(job);  
            }  
            return deleteJob;  
        } catch (SchedulerException e) {  
            log.error("Failed to delete job - {}", jobName, e);  
            return false;  
        }  
    }  
  
    /** 
     * 暂停 
     * 
     * @param jobName  job name 
     * @param jobGroup job group 
     * @return 
     */  
    @Override  
    public boolean pauseJob(String jobName, String jobGroup) {  
        try {  
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobName, jobGroup));  
            return true;  
        } catch (SchedulerException e) {  
            log.error("Failed to pause job - {}", jobName, e);  
            return false;  
        }  
    }  
  
    /** 
     * 恢复 
     * 
     * @param jobName  job name 
     * @param jobGroup job group 
     * @return 
     */  
    @Override  
    public boolean resumeJob(String jobName, String jobGroup) {  
        try {  
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobName, jobGroup));  
            return true;  
        } catch (SchedulerException e) {  
            log.error("Failed to resume job - {}", jobName, e);  
            return false;  
        }  
    }  
  
    @Override  
    public boolean startJobNow(String jobName, String jobGroup) {  
        try {  
            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobName, jobGroup));  
            return true;  
        } catch (SchedulerException e) {  
            log.error("Failed to start new job - {}", jobName, e);  
            return false;  
        }  
    }  
  
    /** 
     * 分页查询 
     * 
     * @param pageable 
     * @param cron     true: cron trigger, false: simple trigger 
     * @return 
     */  
    @Transactional(readOnly = true)// 方法上注解属性会覆盖类注解上的相同属性  
    @Override  
    public Page<Map<String, Object>> findAll(Pageable pageable, Boolean cron) {  
        if (cron) {  
            return schedulerRepository.getJobWithCronTrigger(pageable);  
        } else {  
            return schedulerRepository.getJobWithSimpleTrigger(pageable);  
        }  
    }  
  
    /** 
     * 根据jobName查询单条记录 
     * 
     * @param jobName 
     * @return 
     */  
    @Transactional(readOnly = true)  
    @Override  
    public SchedulerJob findOne(String jobName) {  
        return schedulerRepository.findSchedulerJobByJobName(jobName);  
    }  
  
}  
```

# 9.Dao层

## 9.1.SchedulerRepository

```java
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
     * @param pageable 分页信息 
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
            "where j.SCHED_NAME = 'schedulerFactoryBean' " +  
            "order by ?#{#pageable}",  
            countQuery = "select count(1) " +  
                    "from qrtz_job_details as j " +  
                    "join qrtz_triggers as t " +  
                    "join qrtz_cron_triggers as c  ON j.JOB_NAME = t.JOB_NAME " +  
                    "and t.TRIGGER_NAME = c.TRIGGER_NAME " +  
                    "and t.TRIGGER_GROUP = c.TRIGGER_GROUP " +  
                    "where j.SCHED_NAME = 'schedulerFactoryBean' ",  
            nativeQuery = true)  
    Page<Map<String, Object>> getJobWithSimpleTrigger(Pageable pageable);  
  
    /** 
     * 仅查询cron trigger关联的Job 
     * 不查询simple trigger关联的job 
     * 
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
            "where j.SCHED_NAME = 'schedulerFactoryBean' " +  
            "order by ?#{#pageable}",  
            countQuery = "select count(1) " +  
                    "from qrtz_job_details as j " +  
                    "join qrtz_triggers as t " +  
                    "join qrtz_cron_triggers as c  ON j.JOB_NAME = t.JOB_NAME " +  
                    "and t.TRIGGER_NAME = c.TRIGGER_NAME " +  
                    "and t.TRIGGER_GROUP = c.TRIGGER_GROUP " +  
                    "where j.SCHED_NAME = 'schedulerFactoryBean' ",  
            nativeQuery = true)  
    Page<Map<String, Object>> getJobWithCronTrigger(Pageable pageable);  
  
    /** 
     * 根据JobName查询SchedulerJob 
     * 
     * @param jobName 
     * @return SchedulerJob 
     */  
    SchedulerJob findSchedulerJobByJobName(String jobName);  
}  
```

# 10.网页Vue+ElementUI实现

## 10.1.simple.html

仅对Simple Trigger管理

```html
<!DOCTYPE html>  
<html>  
<head>  
    <meta charset="UTF-8">  
    <title>QuartzDemo</title>  
    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">  
    <script src="https://unpkg.com/vue/dist/vue.js"></script>  
    <script src="https://cdn.bootcss.com/vue-resource/1.5.1/vue-resource.js"></script>  
    <script src="https://unpkg.com/element-ui/lib/index.js"></script>  
  
    <style>  
        #top {  
            background: #20A0FF;  
            padding: 5px;  
            overflow: hidden  
        }  
    </style>  
  
</head>  
<body>  
<div id="test">  
  
    <a href="cron.html">goto simple.html</a>  
  
    <div id="top">  
        <el-button type="text" @click="search" style="color:white">查询</el-button>  
        <el-button type="text" @click="handleadd" style="color:white">添加</el-button>  
        </span>  
    </div>  
  
    <br/>  
  
    <div style="margin-top:15px">  
  
        <el-table  
                ref="testTable"  
                :data="tableData"  
                style="width:100%"  
                border  
        >  
            <el-table-column  
                    prop="JOB_NAME"  
                    label="任务名称"  
                    sortable  
                    show-overflow-tooltip>  
            </el-table-column>  
  
            <el-table-column  
                    prop="JOB_GROUP"  
                    label="任务所在组"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column  
                    prop="JOB_CLASS_NAME"  
                    label="任务类名"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column  
                    prop="TRIGGER_NAME"  
                    label="触发器名称"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column  
                    prop="TRIGGER_GROUP"  
                    label="触发器所在组"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column  
                    prop="REPEAT_INTERVAL"  
                    label="触发间隔(毫秒)"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column  
                    prop="TIMES_TRIGGERED"  
                    label="已触发次数"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column label="操作" width="300">  
                <template scope="scope">  
                    <el-button  
                            size="small"  
                            type="warning"  
                            @click="handlePause(scope.$index, scope.row)">暂停  
                    </el-button>  
  
                    <el-button  
                            size="small"  
                            type="info"  
                            @click="handleResume(scope.$index, scope.row)">恢复  
                    </el-button>  
  
                    <el-button  
                            size="small"  
                            type="danger"  
                            @click="handleDelete(scope.$index, scope.row)">删除  
                    </el-button>  
  
                    <el-button  
                            size="small"  
                            type="success"  
                            @click="handleUpdate(scope.$index, scope.row)">修改  
                    </el-button>  
                </template>  
            </el-table-column>  
        </el-table>  
  
        <div align="center">  
            <el-pagination  
                    @size-change="handleSizeChange"  
                    @current-change="handleCurrentChange"  
                    :current-page="currentPage"  
                    :page-sizes="[10, 20, 30, 40]"  
                    :page-size="pagesize"  
                    layout="total, sizes, prev, pager, next, jumper"  
                    :total="totalCount">  
            </el-pagination>  
        </div>  
    </div>  
  
    <el-dialog title="添加任务" :visible.syn="dialogFormVisible">  
        <el-form :model="form">  
            <el-form-item label="任务名称" label-width="120px" style="width:35%">  
                <el-input v-model="form.jobName" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="任务分组" label-width="120px" style="width:35%">  
                <el-input v-model="form.jobGroup" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="任务类名" label-width="120px" style="width:35%">  
                <el-input v-model="form.jobClass" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="触发器类型" label-width="120px" style="width:35%">  
                <el-switch  
                        v-model="form.cronJob"  
                        active-color="#13ce66"  
                        inactive-color="#ff4949"  
                        active-text="cron"  
                        inactive-text="simple">  
                </el-switch>  
            </el-form-item>  
            <el-form-item label="表达式" v-show="form.cronJob" label-width="120px" style="width:35%">  
                <el-input v-model="form.cronExpression" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="触发间隔(毫秒)" v-show="!form.cronJob" label-width="120px" style="width:35%">  
                <el-input v-model="form.repeatTime" auto-complete="off"></el-input>  
            </el-form-item>  
        </el-form>  
        <div slot="footer" class="dialog-footer">  
            <el-button @click="dialogFormVisible = false">取 消</el-button>  
            <el-button type="primary" @click="add">确 定</el-button>  
        </div>  
    </el-dialog>  
  
    <el-dialog title="修改任务" :visible.syn="updateFormVisible">  
        <el-form :model="updateform">  
            <el-form-item label="任务名称" label-width="120px" style="width:35%">  
                <el-input v-model="updateform.jobName" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="任务分组" label-width="120px" style="width:35%">  
                <el-input v-model="updateform.jobGroup" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="任务类名" label-width="120px" style="width:35%">  
                <el-input v-model="updateform.jobClass" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="触发器类型" label-width="120px" style="width:35%">  
                <el-switch  
                        v-model="updateform.cronJob"  
                        active-color="#13ce66"  
                        inactive-color="#ff4949"  
                        active-text="cron"  
                        inactive-text="simple">  
                </el-switch>  
            </el-form-item>  
            <el-form-item label="表达式" v-show="updateform.cronJob" label-width="120px" style="width:35%">  
                <el-input v-model="updateform.cronExpression" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="触发间隔(毫秒)" v-show="!updateform.cronJob" label-width="120px" style="width:35%">  
                <el-input v-model="updateform.repeatTime" auto-complete="off"></el-input>  
            </el-form-item>  
        </el-form>  
        <div slot="footer" class="dialog-footer">  
            <el-button @click="updateFormVisible = false">取 消</el-button>  
            <el-button type="primary" @click="update">确 定</el-button>  
        </div>  
    </el-dialog>  
  
</div>  
  
<footer align="center">  
    <p>© Quartz 任务管理</p>  
</footer>  
  
<script>  
    var vue = new Vue({  
        el: "#test",  
        data: {  
            //表格当前页数据  
            tableData: [],  
  
            //请求的URL  
            url: '',  
  
            //默认每页数据量  
            pagesize: 10,  
  
            //当前页码  
            currentPage: 1,  
  
            //查询的页码  
            start: 1,  
  
            //默认数据总数  
            totalCount: 1000,  
  
            //添加对话框默认可见性  
            dialogFormVisible: false,  
  
            //修改对话框默认可见性  
            updateFormVisible: false,  
  
            //提交的表单  
            form: {  
                jobName: '',  
                jobGroup: '',  
                jobClass: '',  
                cronJob: false,  
                repeatTime: 0,  
                cronExpression: ''  
            },  
  
            // 修改的表单  
            updateform: {  
                id: 0,  
                jobName: '',  
                jobGroup: '',  
                jobClass: '',  
                cronJob: false,  
                repeatTime: 0,  
                cronExpression: ''  
            },  
        },  
  
        methods: {  
  
            //从服务器读取数据  
            loadData: function (pageNum, pageSize) {  
                this.$http.get('job/queryjob?cron=false&' + 'page=' + pageNum + '&size=' + pageSize).then(function (res) {  
                    console.log(res);  
                    this.tableData = res.body.content;  
                    this.totalCount = res.body.numberOfElements;  
                }, function () {  
                    console.log('failed');  
                });  
            },  
  
            //单行删除  
            handleDelete: function (index, row) {  
                this.$http.post('job/deletejob', {  
                    "jobName": row.JOB_NAME,  
                    "jobGroup": row.JOB_GROUP  
                }, {emulateJSON: true}).then(function (res) {  
                    this.loadData(this.currentPage, this.pagesize);  
                }, function () {  
                    console.log('failed');  
                });  
            },  
  
            //暂停任务  
            handlePause: function (index, row) {  
                this.$http.post('job/pausejob', {  
                    "jobName": row.JOB_NAME,  
                    "jobGroup": row.JOB_GROUP  
                }, {emulateJSON: true}).then(function (res) {  
                    this.loadData(this.currentPage, this.pagesize);  
                }, function () {  
                    console.log('failed');  
                });  
            },  
  
            //恢复任务  
            handleResume: function (index, row) {  
                this.$http.post('job/resumejob', {  
                    "jobName": row.JOB_NAME,  
                    "jobGroup": row.JOB_GROUP  
                }, {emulateJSON: true}).then(function (res) {  
                    this.loadData(this.currentPage, this.pagesize);  
                }, function () {  
                    console.log('failed');  
                });  
            },  
  
            //搜索  
            search: function () {  
                this.loadData(this.currentPage, this.pagesize);  
            },  
  
            //弹出对话框  
            handleadd: function () {  
                this.dialogFormVisible = true;  
            },  
  
            //添加  
            add: function () {  
                this.$http.post('job/addjob', this.form, {  
                    headers: {'Content-Type': "application/json;charset=utf-8"}  
                }).then(function (res) {  
                    this.loadData(this.currentPage, this.pagesize);  
                    this.dialogFormVisible = false;  
                }, function () {  
                    console.log('failed');  
                });  
            },  
  
            //更新  
            handleUpdate: function (index, row) {  
                console.log(row);  
                this.updateFormVisible = true;  
                this.updateform.jobName = row.JOB_NAME;  
                this.updateform.jobGroup = row.JOB_GROUP;  
                this.updateform.jobClass = row.JOB_CLASS_NAME;  
                this.updateform.cronJob = false;  
                this.updateform.repeatTime = row.REPEAT_INTERVAL;  
                this.$http.get('job-info/findOne?jobName=' + row.JOB_NAME).then(function (res) {  
                    this.updateform.id = res.body.id;  
                    this.updateform.cronExpression = row.cronExpression;  
                }, function () {  
                    console.log('failed');  
                });  
                console.log(this.updateform)  
            },  
  
            //更新任务  
            update: function () {  
                this.$http.post('job/reschedulejob', this.updateform, {  
                    headers: {'Content-Type': "application/json;charset=utf-8"}  
                }).then(function (res) {  
                    this.loadData(this.currentPage, this.pagesize);  
                    this.updateFormVisible = false;  
                }, function () {  
                    console.log('failed');  
                });  
  
            },  
  
            //每页显示数据量变更  
            handleSizeChange: function (val) {  
                this.pagesize = val;  
                this.loadData(this.currentPage, this.pagesize);  
            },  
  
            //页码变更  
            handleCurrentChange: function (val) {  
                this.currentPage = val;  
                this.loadData(this.currentPage, this.pagesize);  
            },  
        },  
  
    });  
  
    //载入数据  
    vue.loadData(vue.currentPage, vue.pagesize);  
</script>  
  
</body>  
</html>  
```

## 10.2.cron.html

进队Cron Trigger 管理

```html
<!DOCTYPE html>  
<html>  
<head>  
    <meta charset="UTF-8">  
    <title>QuartzDemo</title>  
    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">  
    <script src="https://unpkg.com/vue/dist/vue.js"></script>  
    <script src="http://cdn.bootcss.com/vue-resource/1.3.4/vue-resource.js"></script>  
    <script src="https://unpkg.com/element-ui/lib/index.js"></script>  
  
    <style>  
        #top {  
            background: #20A0FF;  
            padding: 5px;  
            overflow: hidden  
        }  
    </style>  
  
</head>  
<body>  
<div id="test">  
  
    <a href="simple.html">goto cron.html</a>  
  
    <div id="top">  
        <el-button type="text" @click="search" style="color:white">查询</el-button>  
        <el-button type="text" @click="handleadd" style="color:white">添加</el-button>  
        </span>  
    </div>  
  
    <br/>  
  
    <div style="margin-top:15px">  
  
        <el-table  
                ref="testTable"  
                :data="tableData"  
                style="width:100%"  
                border  
        >  
            <el-table-column  
                    prop="JOB_NAME"  
                    label="任务名称"  
                    sortable  
                    show-overflow-tooltip>  
            </el-table-column>  
  
            <el-table-column  
                    prop="JOB_GROUP"  
                    label="任务所在组"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column  
                    prop="JOB_CLASS_NAME"  
                    label="任务类名"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column  
                    prop="TRIGGER_NAME"  
                    label="触发器名称"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column  
                    prop="TRIGGER_GROUP"  
                    label="触发器所在组"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column  
                    prop="CRON_EXPRESSION"  
                    label="表达式"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column  
                    prop="TIME_ZONE_ID"  
                    label="时区"  
                    sortable>  
            </el-table-column>  
  
            <el-table-column label="操作" width="300">  
                <template scope="scope">  
                    <el-button  
                            size="small"  
                            type="warning"  
                            @click="handlePause(scope.$index, scope.row)">暂停  
                    </el-button>  
  
                    <el-button  
                            size="small"  
                            type="info"  
                            @click="handleResume(scope.$index, scope.row)">恢复  
                    </el-button>  
  
                    <el-button  
                            size="small"  
                            type="danger"  
                            @click="handleDelete(scope.$index, scope.row)">删除  
                    </el-button>  
  
                    <el-button  
                            size="small"  
                            type="success"  
                            @click="handleUpdate(scope.$index, scope.row)">修改  
                    </el-button>  
                </template>  
            </el-table-column>  
        </el-table>  
  
        <div align="center">  
            <el-pagination  
                    @size-change="handleSizeChange"  
                    @current-change="handleCurrentChange"  
                    :current-page="currentPage"  
                    :page-sizes="[10, 20, 30, 40]"  
                    :page-size="pagesize"  
                    layout="total, sizes, prev, pager, next, jumper"  
                    :total="totalCount">  
            </el-pagination>  
        </div>  
    </div>  
  
    <el-dialog title="添加任务" :visible.syn="dialogFormVisible">  
        <el-form :model="form">  
            <el-form-item label="任务名称" label-width="120px" style="width:35%">  
                <el-input v-model="form.jobName" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="任务分组" label-width="120px" style="width:35%">  
                <el-input v-model="form.jobGroup" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="任务类名" label-width="120px" style="width:35%">  
                <el-input v-model="form.jobClass" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="触发器类型" label-width="120px" style="width:35%">  
                <el-switch  
                        v-model="form.cronJob"  
                        active-color="#13ce66"  
                        inactive-color="#ff4949"  
                        active-text="cron"  
                        inactive-text="simple">  
                </el-switch>  
            </el-form-item>  
            <el-form-item label="表达式" v-show="form.cronJob" label-width="120px" style="width:35%">  
                <el-input v-model="form.cronExpression" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="触发间隔(毫秒)" v-show="!form.cronJob" label-width="120px" style="width:35%">  
                <el-input v-model="form.repeatTime" auto-complete="off"></el-input>  
            </el-form-item>  
        </el-form>  
        <div slot="footer" class="dialog-footer">  
            <el-button @click="dialogFormVisible = false">取 消</el-button>  
            <el-button type="primary" @click="add">确 定</el-button>  
        </div>  
    </el-dialog>  
  
    <el-dialog title="修改任务" :visible.syn="updateFormVisible">  
        <el-form :model="updateform">  
            <el-form-item label="任务名称" label-width="120px" style="width:35%">  
                <el-input v-model="updateform.jobName" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="任务分组" label-width="120px" style="width:35%">  
                <el-input v-model="updateform.jobGroup" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="任务类名" label-width="120px" style="width:35%">  
                <el-input v-model="updateform.jobClass" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="触发器类型" label-width="120px" style="width:35%">  
                <el-switch  
                        v-model="updateform.cronJob"  
                        active-color="#13ce66"  
                        inactive-color="#ff4949"  
                        active-text="cron"  
                        inactive-text="simple">  
                </el-switch>  
            </el-form-item>  
            <el-form-item label="表达式" v-show="updateform.cronJob" label-width="120px" style="width:35%">  
                <el-input v-model="updateform.cronExpression" auto-complete="off"></el-input>  
            </el-form-item>  
            <el-form-item label="触发间隔(毫秒)" v-show="!updateform.cronJob" label-width="120px" style="width:35%">  
                <el-input v-model="updateform.repeatTime" auto-complete="off"></el-input>  
            </el-form-item>  
        </el-form>  
        <div slot="footer" class="dialog-footer">  
            <el-button @click="updateFormVisible = false">取 消</el-button>  
            <el-button type="primary" @click="update">确 定</el-button>  
        </div>  
    </el-dialog>  
  
</div>  
  
<footer align="center">  
    <p>© Quartz 任务管理</p>  
</footer>  
  
<script>  
    var vue = new Vue({  
        el: "#test",  
        data: {  
            //表格当前页数据  
            tableData: [],  
  
            //请求的URL  
            url: 'job/queryjob',  
  
            //默认每页数据量  
            pagesize: 10,  
  
            //当前页码  
            currentPage: 1,  
  
            //查询的页码  
            start: 1,  
  
            //默认数据总数  
            totalCount: 1000,  
  
            //添加对话框默认可见性  
            dialogFormVisible: false,  
  
            //修改对话框默认可见性  
            updateFormVisible: false,  
  
            //提交的表单  
            form: {  
                jobName: '',  
                jobGroup: '',  
                jobClass: '',  
                cronJob: true,  
                repeatTime: 0,  
                cronExpression: ''  
            },  
  
            // 修改的表单  
            updateform: {  
                id: 0,  
                jobName: '',  
                jobGroup: '',  
                jobClass: '',  
                cronJob: true,  
                repeatTime: 0,  
                cronExpression: ''  
            },  
        },  
  
        methods: {  
  
            //从服务器读取数据  
            loadData: function (pageNum, pageSize) {  
                this.$http.get('job/queryjob?cron=true&' + 'page=' + pageNum + '&size=' + pageSize).then(function (res) {  
                    console.log(res);  
                    this.tableData = res.body.content;  
                    this.totalCount = res.body.numberOfElements;  
                }, function () {  
                    console.log('failed');  
                });  
            },  
  
            //单行删除  
            handleDelete: function (index, row) {  
                this.$http.post('job/deletejob', {  
                    "jobName": row.JOB_NAME,  
                    "jobGroup": row.JOB_GROUP  
                }, {emulateJSON: true}).then(function (res) {  
                    this.loadData(this.currentPage, this.pagesize);  
                }, function () {  
                    console.log('failed');  
                });  
            },  
  
            //暂停任务  
            handlePause: function (index, row) {  
                this.$http.post('job/pausejob', {  
                    "jobName": row.JOB_NAME,  
                    "jobGroup": row.JOB_GROUP  
                }, {emulateJSON: true}).then(function (res) {  
                    this.loadData(this.currentPage, this.pagesize);  
                }, function () {  
                    console.log('failed');  
                });  
            },  
  
            //恢复任务  
            handleResume: function (index, row) {  
                this.$http.post('job/resumejob', {  
                    "jobName": row.JOB_NAME,  
                    "jobGroup": row.JOB_GROUP  
                }, {emulateJSON: true}).then(function (res) {  
                    this.loadData(this.currentPage, this.pagesize);  
                }, function () {  
                    console.log('failed');  
                });  
            },  
  
            //搜索  
            search: function () {  
                this.loadData(this.currentPage, this.pagesize);  
            },  
  
            //弹出对话框  
            handleadd: function () {  
                this.dialogFormVisible = true;  
            },  
  
            //添加  
            add: function () {  
                this.$http.post('job/addjob', this.form, {  
                    headers: {'Content-Type': "application/json;charset=utf-8"}  
                }).then(function (res) {  
                    this.loadData(this.currentPage, this.pagesize);  
                    this.dialogFormVisible = false;  
                }, function () {  
                    console.log('failed');  
                });  
            },  
  
            //更新  
            handleUpdate: function (index, row) {  
                console.log(row);  
                this.updateFormVisible = true;  
                this.updateform.jobName = row.JOB_NAME;  
                this.updateform.jobGroup = row.JOB_GROUP;  
                this.updateform.jobClass = row.JOB_CLASS_NAME;  
                this.updateform.cronJob = true;  
                this.updateform.cronExpression = row.CRON_EXPRESSION;  
                this.$http.get('job-info/findOne?jobName=' + row.JOB_NAME).then(function (res) {  
                    this.updateform.id = res.body.id;  
                    this.updateform.repeatTime = res.body.repeatTime;  
                }, function () {  
                    console.log('failed');  
                });  
                console.log(this.updateform)  
            },  
  
            //更新任务  
            update: function () {  
                this.$http.post('job/reschedulejob', this.updateform, {  
                    headers: {'Content-Type': "application/json;charset=utf-8"}  
                }).then(function (res) {  
                    this.loadData(this.currentPage, this.pagesize);  
                    this.updateFormVisible = false;  
                }, function () {  
                    console.log('failed');  
                });  
  
            },  
  
            //每页显示数据量变更  
            handleSizeChange: function (val) {  
                this.pagesize = val;  
                this.loadData(this.currentPage, this.pagesize);  
            },  
  
            //页码变更  
            handleCurrentChange: function (val) {  
                this.currentPage = val;  
                this.loadData(this.currentPage, this.pagesize);  
            },  
        },  
  
    });  
    //载入数据  
    vue.loadData(vue.currentPage, vue.pagesize);  
</script>  
  
</body>  
</html>  
```