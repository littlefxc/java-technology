package com.littlefxc.example.activi6.camel;

import org.activiti.camel.ActivitiProducer;
import org.activiti.engine.*;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author fengxuechao
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncCamelTest {
    @Autowired
    SpringCamelContext camelContext;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    ManagementService managementService;

    @Autowired
    HistoryService historyService;

    @Autowired
    ProcessEngine processEngine;

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
                from("activiti:camelAsyncProcess:asyncCamelTask")
                        .log(LoggingLevel.INFO, "Camel路由中接收到流程ID：${property." + ActivitiProducer.PROCESS_ID_PROPERTY + "}")
                        .to("seda:continueAsync");
                from("seda:continueAsync")
                        .log(LoggingLevel.INFO, "Camel路由中接收到流程ID：${property." + ActivitiProducer.PROCESS_ID_PROPERTY + "}")
                        .to("activiti:camelAsyncProcess:receiveAsyncCamel");
            }
        });
    }

    @Test
    public void testAsync() throws InterruptedException {
        ProcessInstance process = runtimeService.startProcessInstanceByKey("camelAsyncProcess");
    }

    @Test
    public void testSignal() {
        taskService.complete("122508");
    }
}
