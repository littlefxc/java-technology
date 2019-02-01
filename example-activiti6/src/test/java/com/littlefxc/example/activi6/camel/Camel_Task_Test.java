package com.littlefxc.example.activi6.camel;

import org.activiti.camel.ActivitiProducer;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author fengxuechao
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Camel_Task_Test {

    @Autowired
    SpringCamelContext camelContext;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    /**
     * 添加路由
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                /* 路由URI */
                from("activiti://leaveWithCamel:invokeCamel?foo=bar")
                        .log(LoggingLevel.INFO, "接收到消息：${property.days}")
                        .log(LoggingLevel.INFO, "Camel路由中接收到流程ID：${property." + ActivitiProducer.PROCESS_ID_PROPERTY + "}")
                        /* 使用流程变量作为条件判断的依据 */
                        .choice().when(simple("${property.days} > 3"))
                        /* 设置deptLeaderAudit变量为true */
                        .setProperty("deptLeaderAudit", simple("true", Boolean.class))
                        /* 设置Activiti流程变量leaderId为部门经理 */
                        .setProperty("leaderId", simple("部门经理", String.class))
                        /* 设置deptLeaderAudit变量为false */
                        .otherwise()
                        .setProperty("deptLeaderAudit", simple("false", Boolean.class))
                        /* 设置Activiti流程变量leaderId为直接领导 */
                        .setProperty("leaderId", simple("直接领导", String.class))
                        /* 此处路由中设置的属性作为相应Body对象的值，这样在流程中就可以使用在路由中设置的属性，这一切都有Activiti的Camel模块来完成 */
                        .end().setBody().exchangeProperties();
            }
        });
    }

    /**
     * 请假
     */
    @Test
    public void testCamel() throws Exception {
        Map<String, Object> vars = new HashMap<>();
        /* 设置请假天数 */
        int days = 5;
        vars.put("days", days);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leaveWithCamel", vars);
        assertNotNull(processInstance);
        String assignee;
        if (days > 3) {
            assignee = "部门经理";
        } else {
            assignee = "直接领导";
        }
        Task task = taskService.createTaskQuery().taskAssignee(assignee).singleResult();
        assertEquals("部门经理审批", task.getName());
        taskService.claim(task.getId(), assignee);
        taskService.complete(task.getId());
        System.out.println("task.executionId = " + task.getExecutionId());

        Task task1 = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .executionId(task.getExecutionId()).singleResult();
        taskService.complete(task1.getId());

        /**
         * 查验
         */
        ProcessInstance hasProcess = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        if (hasProcess == null) {
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .executionId(task.getExecutionId())
                    .list();

            System.err.println("历史节点表：act_hi_actinst");
            list.forEach(activityInstance -> {
                System.out.println("###########################");
                System.out.println("ID            ：" + activityInstance.getId());
                System.out.println("流程ID        ：" + activityInstance.getProcessInstanceId());
                System.out.println("流程执行实例ID：" + activityInstance.getExecutionId());
                System.out.println("流程节点ID    ：" + activityInstance.getActivityId());
                System.out.println("流程节点名    ：" + activityInstance.getActivityName());
                System.out.println("流程任务ID    ：" + activityInstance.getTaskId() + ", 任务代理人：" + activityInstance.getAssignee());
                System.out.println("任务代理人     ：" + activityInstance.getAssignee());
            });
        }

    }

    @Test
    public void testCamel_1() {
        Task task = taskService.createTaskQuery().executionId("20003").singleResult();
        taskService.claim(task.getId(), "部门经理");
        taskService.complete(task.getId());
    }
}
