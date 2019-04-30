package com.fengxuechao.examples.consulclient;

import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringCloudConsulClientApplicationTests {

    static ConsulProcess consul;

    @BeforeClass
    public static void setup() {
        consul = ConsulStarterBuilder.consulStarter().build().start();
        System.setProperty("spring.cloud.consul.enabled", "true");
        System.setProperty("spring.cloud.consul.host", "localhost");
        System.setProperty("spring.cloud.consul.port", String.valueOf(consul.getHttpPort()));
    }

    @Test
    public void contextLoads() {
        String address = consul.getAddress();
        System.out.println(address);
    }

}
