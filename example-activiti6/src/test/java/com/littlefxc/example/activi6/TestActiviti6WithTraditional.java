package com.littlefxc.example.activi6;

import org.activiti.engine.*;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengxuechao
 */
public class TestActiviti6WithTraditional {

    private ProcessEngineConfiguration processEngineConfiguration;

    private ProcessEngine processEngine;

    private RepositoryService repositoryService;

    private RuntimeService runtimeService;

    private RuntimeServiceImpl runtimeServiceImpl = (RuntimeServiceImpl) runtimeService;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        processEngineConfiguration = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/activiti-spring-boot?characterEncoding=utf8&useSSL=false")
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setJdbcUsername("root")
                .setJdbcPassword("123456");

        processEngine = processEngineConfiguration.buildProcessEngine();

        repositoryService = processEngine.getRepositoryService();

        runtimeService = processEngine.getRuntimeService();

        /*DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/activiti-spring-boot?characterEncoding=utf8&useSSL=false");
        source.setUsername("root");
        source.setPassword("123456");
        jdbcTemplate = new JdbcTemplate(source);*/

    }

    /**
     * helloworld流程
     */
    @Test
    public void init() {
        Deployment deploy = repositoryService
                .createDeployment()
                .name("流程定义") // 添加部署的名称
                .addClasspathResource("processes/helloworld.bpmn20.xml")
                .deploy();

        System.err.println("deploy.id             = " + deploy.getId());
        System.err.println("deploy.key            = " + deploy.getKey());
        System.err.println("deploy.name           = " + deploy.getName());
        System.err.println("deploy.category       = " + deploy.getCategory());
        System.err.println("deploy.tenantId       = " + deploy.getTenantId());
        System.err.println("deploy.deploymentTime = " + deploy.getDeploymentTime());

        runtimeService.startProcessInstanceByKey("helloworld");

        completeHelloWorldTask("张三");
        completeHelloWorldTask("李四");
        completeHelloWorldTask("王五");

    }

    public void completeHelloWorldTask(String assignee) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService
                .createTaskQuery().taskAssignee(assignee).list();

        System.err.println("###########");
        list.forEach(task -> {
            System.err.println("task.id = " + task.getId());
            System.err.println("task.name = " + task.getName());
            System.err.println("task.createTime = " + task.getCreateTime());
            System.err.println("task.assignee = " + task.getAssignee());
            System.err.println("task.processInstanceId = " + task.getProcessInstanceId());
            System.err.println("task.executionId = " + task.getExecutionId());
            System.err.println("task.processDefinitionId = " + task.getProcessDefinitionId());
        });

        String taskId = list.get(0).getId();

        taskService.complete(taskId);
        System.err.println("Complete task");
    }

    /**
     * ReceiceTask任务，机器自动完成的任务
     * 只会在act_ru_execution表中产生一条数据
     *
     * @throws Exception
     */

    @Test
    public void testExecution() throws Exception {

        RuntimeService runtimeService = processEngine.getRuntimeService();

        RuntimeServiceImpl runtimeServiceImpl = (RuntimeServiceImpl) runtimeService;

        // 1 发布流程
        processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("processes/receiveTaskDemo.bpmn20.xml")
                .deploy();

        // 2 启动流程
        ProcessInstance pi = runtimeService
                .startProcessInstanceByKey("receiveTaskDemo");
        System.out.println("pid:" + pi.getId());
        String pid = pi.getId();

        // 3查询是否有一个执行对象在描述”汇总当日销售额“
        Execution e1 = runtimeService
                .createExecutionQuery()
                .processInstanceId(pid)
                .activityId("汇总当日销售额")
                .singleResult();

        // 4执行一堆逻辑，并设置流程变量
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("当日销售额", 10000);
        //  5流程向后执行一步：往后推移e1,使用signal给流程引擎信号，告诉他当前任务已经完成了，可以往后执行
        runtimeServiceImpl.signal(e1.getId(), vars);

        // 6判断当前流程是否在”给老板发短信“节点
        Execution e2 = runtimeService
                .createExecutionQuery()
                .processInstanceId(pid)
                .activityId("给总经理发短信")
                .singleResult();

        // 7获取流程变量
        Integer money = (Integer) runtimeService//
                .getVariable(e2.getId(), "当日销售额");
        System.out.println("老板，今天赚了" + money);
        // 8向后执行一步：任务完成，往后推移”给老板发短信“任务
        runtimeServiceImpl.signal(e2.getId());


        // 9查询流程状态
        pi = runtimeService//
                .createProcessInstanceQuery()
                .processInstanceId(pid)
                .singleResult();
        if (pi == null) {
            System.out.println("流程正常执行！！！，已经结束了");
        }
    }


}
