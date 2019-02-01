---
title: "Activiti6 学习笔记"
author: 冯雪超
date: October 17, 2018
output: word_document
---

# Activiti6 学习笔记

## 1 数据库表结构说明

### 1.1 数据库命名

Activiti 数据库中表的命名都是以 ACT_开头的。第二部分是一个两个字符用例表的标识。此用例大体与服务 API 是匹配的。

- ACT_RE_*：'RE'代表 repository。带此前缀的表包含的是静态信息，如，流程定义、流程的资源（图片、规则，等）。
- ACT_RU_*：'RU'代表 runtime。就是这个运行时的表存储着流程变量、用户任务、变量、作业，等中的运行时的数据。Activiti 只存储流程实例执行期间的运行时数据，当流程实例结束时，将删除这些记录。这就使这些运行时的表保持的小且快。
- ACT_ID_*：'ID'代表 identity。这些表包含着标识的信息，如用户、用户组、等等。
- ACT_HI_*：'HI'代表 history。就是这些表包含着历史的相关数据，如结束的流程实例、变量、任务、等等。
- ACT_GE_*：普通数据，各种情况都使用的数据。

### 1.2 数据库表结构说明

![activiti6-sql_1.png](example-activiti-parent/md/activiti6-sql_1.png)

![activiti6-sql_2.png](example-activiti-parent/md/activiti6-sql_2.png)

#### 1.2.1 资源库流程规则表

| 表名 | 描述 |
| ----------------- | ---------------- |
| act_re_deployment | 部署信息表        |
| act_re_model      | 流程设计模型部署表 |
| act_re_procdef    | 流程定义数据表     |

#### 1.2.2 运行时数据库表

| 表名 | 描述 |
| --------------------- | ---------------- |
| act_ru_execution		| 运行时流程执行实例表 |
| act_ru_identitylink	| 运行时流程人员表，主要存储任务节点与参与者的相关信息 |
| act_ru_task			| 运行时任务节点表 |
| act_ru_variable		| 运行时流程变量数据表 |

#### 1.2.3 历史数据库表

| 表名 | 描述 |
| --------------------- | ---------------- |
|act_hi_actinst 		| 历史节点表 |
|act_hi_attachment		| 历史附件表 |
|act_hi_comment		    | 历史意见表 |
|act_hi_identitylink	| 历史流程人员表 |
|act_hi_detail			| 历史详情表，提供历史变量的查询 |
|act_hi_procinst		| 历史流程实例表 |
|act_hi_taskinst		| 历史任务实例表 |
|act_hi_varinst			| 历史变量表 |

#### 1.2.4 组织机构表

| 表名 | 描述 |
| ----------------- | ---------------- |
| act_id_group		| 用户组信息表 |
| act_id_info		| 用户扩展信息表 |
| act_id_membership	| 用户与用户组对应信息表 |
| act_id_user		| 用户信息表 |

这四张表很常见，基本的组织机构管理，关于用户认证方面建议还是自己开发一套，组件自带的功能太简单，使用中有很多需求难以满足

#### 1.2.5 通用数据表

| 表名 | 描述 |
| --------------------- | ---------------- |
| act_ge_bytearray		| 二进制数据表 |
| act_ge_property		| 属性数据表存储整个流程引擎级别的数据,初始化表结构时，会默认插入三条记录 |

### 1.3 数据库连接配置

#### 1.3.1 activiti-spring-boot-starter-basic的有关数据库的源码分析

打开`activiti-spring-boot-starter-basic`依赖包查看源码结构可以从类名上看到两个明显有关数据库自动配置的类，见下图：

![activiti-spring-boot-starter-basic源码结构](example-activiti-parent/md/activiti-spring-boot-starter-basic源码结构.png)

无论使用哪种下面的两个自动配置类，在Spring Boot 项目中需要主动配置的就只是实现`javax.sql.DataSource`。

##### 1.3.1.1 JpaProcessEngineAutoConfiguration

```Java
package org.activiti.spring.boot;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 1. 自动配置该类在DataSourceProcessEngineAutoConfiguration之前、在DataSourceAutoConfiguration之后；
 * 2. 如果配置了JPA，就使用JPA事务管理
 * 3. 使用SpringProcessEngineConfiguration来初始化流程引擎实例（流程引擎初始化有Activiti配置风格和Spring配置风格）
 * @author Joram Barrez
 * @author Josh Long
 */
@Configuration
@AutoConfigureBefore(DataSourceProcessEngineAutoConfiguration.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class JpaProcessEngineAutoConfiguration {

  /**
   * 当前Classpath中存在javax.persistence.EntityManagerFactory(JPA 实体管理器工厂)这个类是创建该配置类。
   * javax.persistence.EntityManagerFactory存在于（org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.0.Final）
   */
  @Configuration
  @ConditionalOnClass(name= "javax.persistence.EntityManagerFactory")
  @EnableConfigurationProperties(ActivitiProperties.class)
  public static class JpaConfiguration extends AbstractProcessEngineAutoConfiguration {

    /**
     * JPA Hibernate
     * org.springframework.orm.jpa.JpaTransactionManager
     */
    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
      return new JpaTransactionManager(emf);
    }

    /**
     * @dataSource 看到这个参数就可知，只需在Spring Boot项目中实现javax.sql.DataSource接口，Activiti的数据库连接就会自动装配
     */
    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            DataSource dataSource, EntityManagerFactory entityManagerFactory,
            PlatformTransactionManager transactionManager, SpringAsyncExecutor springAsyncExecutor) throws IOException {

      SpringProcessEngineConfiguration config = this.baseSpringProcessEngineConfiguration(dataSource, 
          transactionManager, springAsyncExecutor);
      config.setJpaEntityManagerFactory(entityManagerFactory);
      config.setTransactionManager(transactionManager);
      config.setJpaHandleTransaction(false);
      config.setJpaCloseEntityManager(false);
      return config;
    }
  }

}
```

##### 1.3.1.2 DataSourceProcessEngineAutoConfiguration**

```Java
package org.activiti.spring.boot;

import java.io.IOException;

import javax.sql.DataSource;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 1. 自动配置该类在DataSourceAutoConfiguration之后；
 * 2. 如果没有配置JPA，就使用Spring事务管理
 * 3. 使用SpringProcessEngineConfiguration来初始化流程引擎实例（流程引擎初始化有Activiti配置风格和Spring配置风格）
 * @author Joram Barrez
 * @author Josh Long
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class DataSourceProcessEngineAutoConfiguration {

  /**
   * 当前Classpath中不存在javax.persistence.EntityManagerFactory(JPA 实体管理器工厂)这个类是创建该配置类。
   * javax.persistence.EntityManagerFactory存在于（org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.0.Final）
   */
  @Configuration
  @ConditionalOnMissingClass(name= "javax.persistence.EntityManagerFactory")
  @EnableConfigurationProperties(ActivitiProperties.class)
  public static class DataSourceProcessEngineConfiguration extends AbstractProcessEngineAutoConfiguration {

    /**
     * Mybatis
     * org.springframework.transaction.PlatformTransactionManager
     */
    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
      return new DataSourceTransactionManager(dataSource);
    }

    /**
     * @dataSource 看到这个参数就可知，只需在Spring Boot项目中实现javax.sql.DataSource接口，Activiti的数据库连接就会自动装配
     */
    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            DataSource dataSource,
            PlatformTransactionManager transactionManager,
            SpringAsyncExecutor springAsyncExecutor) throws IOException {

      return this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
    }
  }

}
```

##### 1.3.1.3 验证

在该示例项目中，我使用了JPA,依据上文可得知`JpaProcessEngineAutoConfiguration`能够实例化，而不是`DataSourceProcessEngineAutoConfiguration`

打个断点，查看结果：  
![JpaProcessEngineAutoConfiguration断点结果.png](example-activiti-parent/md/JpaProcessEngineAutoConfiguration断点结果.png)

## 2 工作流引擎

ProcessEngine对象，这是Activiti工作的核心。负责生成流程运行时的各种实例及数据、监控和管理流程的运行。

### 2.1 核心API

#### 2.1.1 ProcessEngine

- 在Activiti中最核心的类， 其他的类都有它而来。
- 由`ProcessEngine`产生的各个Service的作用

    | 表名 | 描述 |
    | ----------------------------------------------| ---------- |
    | <font color=red>**RepositoryService**</font>  | <font color=red>**管理流程定义**</font> |
    | <font color=red>**RuntimeService**</font>     | <font color=red>**执行管理，包括启动、推进、删除流程实例等操作**</font> |
    | **TaskService**                               | **任务管理** |
    | **HistoryService**                            | **历史管理(执行完的数据的管理)** |
    | **IdentityService**                           | **组织机构管理** |
    | FormService                                   | 一个可选服务，任务表单管理 |
    | ManagerService                                |   |

#### 2.1.2 RepositoryService

是Activiti的仓库服务类。所谓的仓库指流程定义文档的两个文件：bpmn文件和流程图片。

1. 产生方式  
    ```Java
    RepositoryService repositoryService = processEngine.getRepositoryService();
    ```
2. 可以产生DeploymentBuilder，用来定义流程部署的相关参数
    ```Java
    DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
    ```
3. 删除流程定义
    ```Java
    repositoryService.deleteDeployment(deleteDeploymentId)
    ```

#### 2.1.3 RuntimeService

是activiti的流程执行服务类。可以从这个服务类中获取很多关于流程执行相关的信息。

#### 2.1.4 TaskService

是activiti的任务服务类。可以从这个类中获取任务的信息。

#### 2.1.5 HistoryService

是activiti的查询历史信息的类。在一个流程执行完成后，这个对象为我们提供查询历史信息。

#### 2.1.6 ProcessDefinition

流程定义类。可以从这里获得资源文件等。

#### 2.1.7 ProcessInstance

代表流程定义的执行实例。如小红请了一天的假，她就必须发出一个流程实例的申请。一个流程实例包括了所有的运行节点。我们可以利用这个对象来了解当前流程实例的进度等信息。**流程实例就表示一个流程从开始到结束的最大的流程分支**，即一个流程中流程实例只有一个。

#### 2.1.8 Execution

Activiti用这个对象去描述流程执行的每一个节点。在没有并发的情况下，Execution就是同ProcessInstance。**流程按照流程定义的规则执行一次的过程**，就可以表示执行对象Execution。下图为`ProcessInstance`的源码：  
![ProcessInstance.png](example-activiti-parent/md/ProcessInstance.png)  
从源代码中可以看出ProcessInstance就是Execution。但在现实意义上有所区别：  

- 一个流程中，执行对象可以存在多个，但是流程实例只能有一个。
- 当流程按照规则只执行一次的时候，那么流程实例就是执行对象。

## 3 BPMN

## 4 Example - Spring Boot + Activiti6

### 4.1 创建pom.xml

要在Spring Boot中使用Activiti6, 较为重要的这几个依赖：  

```xml
<!-- 引入activiti6 -->
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-spring-boot-starter-basic</artifactId>
    <version>6.0.0</version>
</dependency>
<!-- activiti6 支持mysql -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
<!-- 主要为activiti6创建dataSource -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.10</version>
</dependency>
<!-- 配置ORM, 配合JpaProcessEngineAutoConfiguration创建流程实例 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

## 4.2 流程图

![example-流程图](example-activiti-parent/md/example-流程图.png)

## 4.3 配置数据源

1. 配置阿里巴巴的连接池依赖  
    ```xml
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.10</version>
    </dependency>
    ```

2. 数据源dataSource

    application-druid.yml  

    ```YAML
    spring:
      datasource:
        type: com.alibaba.druid.pool.DruidDataSource
        druid:
          url: jdbc:mysql://localhost:3306/activiti-spring-boot?    characterEncoding=utf8&useSSL=false
          username: root
          password: 123456
          driver-class-name: com.mysql.jdbc.Driver
          initial-size: 1
          max-active: 20
          min-idle: 1
          max-wait: 60000
          validation-query: SELECT 'x'
          test-on-borrow: false
          test-on-return: false
          test-while-idle: true
          time-between-eviction-runs-millis: 60000
          min-evictable-idle-time-millis: 300000
    ```

## 4.4 部署流程定义

`activiti-spring-boot-starter-basic`这个依赖是自动配置的，前文也有分析，它是如何创建流程的, 查看源码:

```Java
@ConfigurationProperties("spring.activiti")
public class ActivitiProperties {

  private boolean checkProcessDefinitions = true;
  private boolean asyncExecutorActivate = true;
  private boolean restApiEnabled;
  private String deploymentName;
  private String mailServerHost = "localhost";
  private int mailServerPort = 1025;
  private String mailServerUserName;
  private String mailServerPassword;
  private String mailServerDefaultFrom;
  private boolean mailServerUseSsl;
  private boolean mailServerUseTls;
  private String databaseSchemaUpdate = "true";
  private String databaseSchema;
  private boolean isDbIdentityUsed = true;
  private boolean isDbHistoryUsed = true;
  private HistoryLevel historyLevel = HistoryLevel.AUDIT;
  private String processDefinitionLocationPrefix = "classpath:/processes/";
  private List<String> processDefinitionLocationSuffixes = Arrays.asList("**.bpmn20.xml", "**.bpmn");
  private String restApiMapping = "/api/*";
  private String restApiServletName = "activitiRestApi";
  private boolean jpaEnabled = true; // true by default
  private List<String> customMybatisMappers;
  private List<String> customMybatisXMLMappers;
```

可以从中看到`activiti-spring-boot-starter-basic`定义了默认的流程定义文件的格式和默认流程部署文件夹`classpath:/processes/`

## 4.5 关键代码

### 4.5.1 实体类

User.java

```Java
/**
 * 用户信息表
 */
@Data
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户身份标识（1-申请者，2-审核者）
     */
    private Integer type;

    private Integer delete_flag;
}
```

VacationForm.java

```Java
/**
 * 请假单信息表
 */
@Data
@Entity
@Table(name = "vacation_form")
public class VacationForm implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String content;

    /**
     * 申请者
     */
    private String applicant;

    /**
     * 审批者
     */
    private String approver;

    /**
     * 申请所处状态
     */
    @Transient
    private String state;
}
```

### 4.5.2 业务层

```Java
package com.littlefxc.example.activi6.service.Impl;

import com.littlefxc.example.activi6.entity.User;
import com.littlefxc.example.activi6.entity.VacationForm;
import com.littlefxc.example.activi6.service.MiaoService;
import com.littlefxc.example.activi6.service.UserService;
import com.littlefxc.example.activi6.service.VacationFormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("miaoService")
public class MiaoServiceImpl implements MiaoService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Autowired
    private VacationFormService vacationFormService;

    @Autowired
    private UserService userService;

    //填写请假信息
    @Override
    public VacationForm writeForm(String title, String content, String applicant) {
        VacationForm form = new VacationForm();
        String approver = "未知审批者";
        form.setTitle(title);
        form.setContent(content);
        form.setApplicant(applicant);
        form.setApprover(approver);
        vacationFormService.save(form);

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", form.getApplicant());
        //开始请假流程，使用formId作为流程的businessKey
        runtimeService.startProcessInstanceByKey("vacation", form.getId().toString(), variables);
        return form;
    }

    //根据选择，申请或放弃请假
    @Override
    public void completeProcess(String formId, String operator, String input) {
        //根据businessKey找到当前任务节点
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(formId).singleResult();
        //设置输入参数，使流程自动流转到对应节点
        taskService.setVariable(task.getId(), "input", input);
        taskService.complete(task.getId());
        if ("apply".equals(input)) {
            applyVacation(formId, operator);
        } else {
            giveupVacation(formId, operator);
        }
    }

    //放弃请假
    @Override
    public boolean giveupVacation(String formId, String operator) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(formId).singleResult();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", operator);
        //认领任务
        taskService.claim(task.getId(), operator);
        //完成任务
        taskService.complete(task.getId(), variables);
        return true;
    }

    @Override
    public boolean applyVacation(String formId, String operator) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(formId).singleResult();
        Map<String, Object> variables = new HashMap<String, Object>();
        List<User> users = userService.findAll();
        String managers = "";
        //获取所有具有审核权限的用户
        for (User user : users) {
            if (user.getType().equals(2)) {
                managers += user.getName() + ",";
            }
        }
        managers = managers.substring(0, managers.length() - 1);
        variables.put("employee", operator);
        variables.put("managers", managers);
        taskService.claim(task.getId(), operator);
        taskService.complete(task.getId(), variables);
        return true;
    }

    @Override
    public boolean approverVacation(String formId, String operator) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(formId).singleResult();
        taskService.claim(task.getId(), operator);
        taskService.complete(task.getId());
        //更新请假信息的审核人
        VacationForm form = vacationFormService.findOne(Integer.parseInt(formId));
        if (form != null) {
            form.setApprover(operator);
            vacationFormService.save(form);
        }
        return true;
    }

    //获取请假信息的当前流程状态
    @Override
    public HashMap<String, String> getCurrentState(String formId) {
        HashMap<String, String> map = new HashMap<String, String>();
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(formId).singleResult();
        if (task != null) {
            map.put("status", "processing");
            map.put("taskId", task.getId());
            map.put("taskName", task.getName());
            map.put("user", task.getAssignee());
        } else {
            map.put("status", "finish");
        }
        return map;
    }

    //请假列表
    @Override
    public List<VacationForm> formList() {
        List<VacationForm> formList = vacationFormService.findAll();
        for (VacationForm form : formList) {
            Task task = taskService.createTaskQuery().processInstanceBusinessKey(form.getId().toString())
                    .singleResult();
            if (task != null) {
                String state = task.getName();
                form.setState(state);
            } else {
                form.setState("已结束");
            }
        }
        return formList;
    }

    //登录验证用户名是否存在
    @Override
    public User loginSuccess(String username) {
        List<User> users = userService.findByName(username);
        if (users != null && users.size() > 0) {
            User user = users.get(0);
            return user;
        }
        return null;
    }

    //获取当前登录用户
    public String getCurrentUser(HttpServletRequest request) {
        String user = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userInfo")) {
                    user = cookie.getValue();
                }
            }
        }
        return user;
    }

    //获取已执行的流程信息
    @Override
    public List historyState(String formId) {
        List<HashMap<String, String>> processList = new ArrayList<HashMap<String, String>>();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(formId).list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance hti : list) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", hti.getName());
                map.put("operator", hti.getAssignee());
                processList.add(map);
            }
        }
        return processList;
    }
}
```

### 4.5.3 控制器

```Java
package com.littlefxc.example.activi6.controller;

import com.littlefxc.example.activi6.entity.User;
import com.littlefxc.example.activi6.entity.VacationForm;
import com.littlefxc.example.activi6.service.MiaoService;
import com.littlefxc.example.activi6.util.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author fengxuechao
 */
@Controller
public class MiaoController {

    @Autowired
    private MiaoService miaoService;

    @RequestMapping("/")
    public String login() {
        return "login";
    }

    //申请者的首页
    @RequestMapping("/home")
    public String index(ModelMap model, HttpServletRequest request) {
        List<VacationForm> forms = miaoService.formList();
        Cookie[] cookies = request.getCookies();
        String user = "";
        //从cookie中获取当前用户
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userInfo".equals(cookie.getName())) {
                    user = cookie.getValue();
                    break;
                }
            }
        }
        List<VacationForm> formsMap = new ArrayList<VacationForm>();
        for (VacationForm form : forms) {
            //申请者只能看到自己申请的请假单信息
            if (user.equals(form.getApplicant())) {
                formsMap.add(form);
            }
        }
        //将forms参数返回
        model.addAttribute("forms", formsMap);
        return "index";
    }

    //审核者的首页
    @RequestMapping("/homeApprover")
    public String indexApprover(ModelMap model) {
        List<VacationForm> forms = miaoService.formList();
        List<VacationForm> formsMap = new ArrayList<VacationForm>();
        for (VacationForm form : forms) {
            //审核者只能看到待审核状态的请假单
            if ("领导审核".equals(form.getState())) {
                formsMap.add(form);
            }
        }
        model.addAttribute("forms", formsMap);
        return "indexApprover";
    }

    //请假单页面
    @RequestMapping("/form")
    public String form() {
        return "form";
    }

    @ResponseBody
    @PostMapping("/login")
    public ResultInfo login(HttpServletRequest request, HttpServletResponse response) {
        ResultInfo result = new ResultInfo();
        String username = request.getParameter("username");
        User user = miaoService.loginSuccess(username);
        if (user != null) {
            result.setCode(200);
            result.setMsg("登录成功");
            result.setInfo(user);
            //用户信息存放在Cookie中，实际情况下保存在Redis更佳
            Cookie cookie = new Cookie("userInfo", username);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            result.setCode(300);
            result.setMsg("登录名不存在，登录失败");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/logout")
    public ResultInfo logout(HttpServletRequest request, HttpServletResponse response) {
        ResultInfo result = new ResultInfo();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userInfo")) {
                    cookie.setValue(null);
                    // 立即销毁cookie
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        result.setCode(200);
        return result;
    }

    //添加请假单
    @ResponseBody
    @RequestMapping("/writeForm")
    public ResultInfo writeForm(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String operator) {
        ResultInfo result = new ResultInfo();
        VacationForm form = miaoService.writeForm(title, content, operator);
        result.setCode(200);
        result.setMsg("填写请假条成功");
        result.setInfo(form);
        return result;
    }

    //申请者放弃请假
    @ResponseBody
    @RequestMapping("/giveup")
    public ResultInfo giveup(HttpServletRequest request) {
        ResultInfo result = new ResultInfo();
        String formId = request.getParameter("formId");
        String operator = request.getParameter("operator");
        miaoService.completeProcess(formId, operator, "giveup");
        result.setCode(200);
        result.setMsg("放弃请假成功");
        return result;
    }

    //申请者申请请假
    @RequestMapping("/apply")
    @ResponseBody
    public ResultInfo apply(HttpServletRequest request) {
        ResultInfo result = new ResultInfo();
        String formId = request.getParameter("formId");
        String operator = request.getParameter("operator");
        miaoService.completeProcess(formId, operator, "apply");
        result.setCode(200);
        result.setMsg("申请请假成功");
        return result;
    }

    //审批者审核请假信息
    @ResponseBody
    @RequestMapping("/approve")
    public ResultInfo approve(HttpServletRequest request) {
        ResultInfo result = new ResultInfo();
        String formId = request.getParameter("formId");
        String operator = request.getParameter("operator");
        miaoService.approverVacation(formId, operator);
        result.setCode(200);
        result.setMsg("请假审核成功");
        return result;
    }

    //获取某条请假信息当前状态
    @ResponseBody
    @RequestMapping("/currentState")
    public HashMap<String, String> currentState(HttpServletRequest request) {
        String formId = request.getParameter("formId");
        HashMap<String, String> map = new HashMap<String, String>();
        map = miaoService.getCurrentState(formId);
        return map;
    }

    @ResponseBody
    @RequestMapping("/historyState")
    public ResultInfo queryHistoricTask(HttpServletRequest request) {
        ResultInfo result = new ResultInfo();
        String formId = request.getParameter("formId");
        List process = miaoService.historyState(formId);
        result.setCode(200);
        result.setInfo(process);
        return result;
    }
}
```

## 4.6 效果图

登录界面：

![login.png](example-activiti-parent/md/login.png)  

主页：

![home.png](example-activiti-parent/md/home.png)  

流程步骤图：

![history.png](example-activiti-parent/md/history.png)  

领导审核：

![check.png](example-activiti-parent/md/check.png)