package com.littlefxc.examples.cloud;

import org.springframework.stereotype.Component;

/**
 * @author fengxuechao
 * @date 2019/2/25
 **/
@Component
public class HelloServiceFallback implements HelloService {

    @Override
    public String hello() {
        return "feign error";
    }

}
