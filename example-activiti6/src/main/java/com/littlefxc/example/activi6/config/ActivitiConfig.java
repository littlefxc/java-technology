package com.littlefxc.example.activi6.config;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengxuechao
 */
@Slf4j
@Configuration
public class ActivitiConfig {

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;
}
