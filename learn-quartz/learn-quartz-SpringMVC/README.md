# learn-quartz-SpringBoot

学习如何使用spring-boot-starter-quartz

## 版本

SpringBoot : 2.0.4.RELEASE

JDK : 1.8+

MySQL : 5.7

##  概念

- Trigger 代表了调度的时机
- Job 代表了调度的任务
- Scheduler 代表了在 Trigger 指定的时机调度 JobDetail 指定的任务

以"每天早上六点半叫我起床"为例，"每天早上六点半"对应着 Trigger，"叫我起床"对应着 Job，而⏰对应着 Scheduler

## 依赖配置

```xml
<dependency>  
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency> 
```

配置文件
```
spring.datasource.url=jdbc:mysql://localhost:3306/learn-quartz-springboot?useSSL=false&useUnicode=true&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

# 必须现有dataSource, 数据库脚本代码在org.quartz.impl.jdbcjobstore包下
spring.quartz.job-store-type=jdbc
# 默认每次重启删除现有数据库，然后初始化数据库，
spring.quartz.jdbc.initialize-schema=always
```

属性 spring.quartz.job-store-type 定义了 JobStore 的类型，默认值为 memory 存储在内存中（易失），可选配置为 jdbc 存储在 spring.datasource 配置的数据源中（非易失）

最后，创建 JDBC 依赖的表

在 org.quartz.impl.jdbcjobstore 包下找到 tables_mysql_innodb.sql 文件，可以执行该 SQL 文件创建数据库

## Trigger

Quartz 提供了构建器 TriggerBuilder 可以帮助我们方便得创建 Trigger 实例

Trigger 的几个关键属性：

- TriggerKey 在 Scheduler 中用于唯一标识 Trigger，由 name 和 group 组成，group 不指定默认为 TriggerKey.DEFAULT_GROUP
- 开始时间，Trigger 从何时开始生效
- 结束时间，Trigger 从何时开始失效
- ScheduleBuilder，Trigger 调度规则

ScheduleBuilder 有以下四个子类：

- CalendarIntervalScheduleBuilder
- CronScheduleBuilder
- DailyTimeIntervalScheduleBuilder
- SimpleScheduleBuilder

### CalendarIntervalScheduleBuilder

CalendarIntervalScheduleBuilder 定义了以年、月、日、小时、分钟和秒为周期的调度规则

```
withIntervalInSeconds(10); // 每 10 秒  
withIntervalInDays(3); // 每 3 天  
```

### CronScheduleBuilder

CronScheduleBuilder 定义了基于 Cron 表达式的调度规则

```
cronSchedule("0 */10 * ? * *"); // 每 10 分钟  
```

这里有一个在线生成 Cron 表达式的网站，可以参考：[http://cron.qqe2.com/](http://cron.qqe2.com/)

### DailyTimeIntervalScheduleBuilder

DailyTimeIntervalScheduleBuilder 定义了以天、小时、分钟和秒为周期的调度规则

```
onMondayThroughFriday() // 工作日  
onSaturdayAndSunday() // 周末  
```

### SimpleScheduleBuilder

SimpleScheduleBuilder 定义了严格的字面的周期调度

```
repeatMinutelyForever() // 每分钟 
```
 
 ## Job
 
 首先，定义 Job 类
 
 ```java
@Component // ①
public class TestJob extends QuartzJobBean { // ②

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // ③
    }

}
```

① 使用 @Component 注解标注

② 继承 QuartzJobBean 抽象类，并实现 executeInternal 方法

③ 在 executeInternal 方法内部，实现具体 Job 功能，从 JobExecutionContext 可以获取上下文信息，比如 JobDataMap

Quartz 提供了构建器 JobBuilder 可以帮助我们方便得创建 JobDetail 实例

JobDetail 的几个关键属性：

- JobKey 在 Scheduler 中用于唯一标识 JobDetail，由 name 和 group 组成，group 不指定默认为 JobKey.DEFAULT_GROUP
- JobData 传入的 JobData 数据，可在任务执行时通过 JobExecutionContext 获取

```
JobDetail job = newJob(TestJob.class)  
    .withIdentity("test")
    .usingJobData("id", "1")
    .build();
```

### Job状态和并发
   
JobDetail接口有以下两个方法：

```
boolean isPersistJobDataAfterExecution();

boolean isConcurrentExectionDisallowed();
```

这里有两个注解（写在Job接口的实现类上）分别对应这两个方法。

**@DisallowConcurrentExecution**
这个注解告诉Quartz，一个给定的Job定义（也就是一个JobDetail实例），不并发运行。

**@PersistJobDataAfterExecution**
意为：执行完后持久化JobData。这样当下次执行同样的JobDetail的时候，就会取得上一次执行后更新的数据。它也是作用与一个Job定义（也就是一个JobDetail实例）。

### 其它数据

同样是JobDetail接口里面的方法：
    
```
boolean isDurable();
boolean requestsRecovery();
```
    
**Durability**
一个Job是否是持久的标识。如果一个Job不是持久的，如果在scheduler里，没有了任何active状态的trigger跟它关联，那么就会被自动删除。

**RequestsRecovery**
如果一个Job是“RequestsRecovery”的，它执行过程中，scheduler被“中断”了，比如程序意外崩溃等。当scheduler重新开始后，这个Job也会重新执行。

## Scheduler

使用 spring-boot-starter-quartz 通过依赖注入即可使用 Scheduler，看👇的🌰：

```
@Autowired
private Scheduler scheduler; 
```

如果想修改 Quartz 属性，可以通过修改 application.yaml 的 spring.quartz.properties 实现，
详细配置参数，参考：[Quartz配置参考](https://www.w3cschool.cn/quartz_doc/quartz_doc-i7oc2d9l.html)