package com.fengxuechao.examples.consulclient;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;

public class GatewayRegister extends ConsulServiceRegistry {

    public GatewayRegister(ConsulClient client, ConsulDiscoveryProperties properties, TtlScheduler ttlScheduler, HeartbeatProperties heartbeatProperties) {
        super(client, properties, ttlScheduler, heartbeatProperties);
    }

    @Override
    public void register(ConsulRegistration reg) {
        //重新设计id，此处用的是名字也可以用其他方式例如instanceid、host、uri等
        reg.getService().setId(reg.getService().getName() + "-" + reg.getService().getAddress() + "-" + reg.getPort());
        super.register(reg);
    }
}