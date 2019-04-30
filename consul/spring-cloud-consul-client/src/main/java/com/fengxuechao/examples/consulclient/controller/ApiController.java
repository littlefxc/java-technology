package com.fengxuechao.examples.consulclient.controller;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.Check;
import com.ecwid.consul.v1.agent.model.Member;
import com.ecwid.consul.v1.agent.model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/4/30
 */
@RestController
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final ConsulClient consulClient;
    private final ConsulRegistration consulRegistration;

    @Autowired
    public ApiController(ConsulClient consulClient, ConsulRegistration consulRegistration) {
        this.consulClient = consulClient;
        this.consulRegistration = consulRegistration;
    }

    @GetMapping("api/deregister")
    public void deregister() {
        log.info("***********************consul上无效服务清理开始*******************************************");
        //获取所有的services检查信息
        Iterator<Map.Entry<String, Check>> it = consulClient.getAgentChecks().getValue().entrySet().iterator();
        Map.Entry<String, Check> serviceMap;
        while (it.hasNext()) {
            //迭代数据
            serviceMap = it.next();
            //获取服务名称
            String serviceName = serviceMap.getValue().getServiceName();
            //获取服务ID
            String serviceId = serviceMap.getValue().getServiceId();
            log.info("服务名称 :{}**服务ID:{}", serviceName, serviceId);
            //获取健康状态值  PASSING：正常  WARNING  CRITICAL  UNKNOWN：不正常
            log.info("服务 :{}的健康状态值：{}", serviceName, serviceMap.getValue().getStatus());
            if ( serviceMap.getValue().getStatus() == Check.CheckStatus.CRITICAL) {
                log.info("服务 :{}为无效服务，准备清理...................", serviceName);
                consulClient.agentServiceDeregister(serviceId);
            }
        }
    }
}
