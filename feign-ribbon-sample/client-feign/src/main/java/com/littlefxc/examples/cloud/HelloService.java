package com.littlefxc.examples.cloud;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author fengxuechao
 * @date 2019/2/25
 **/
@FeignClient(value = "say-hello", fallback = HelloServiceFallback.class)
public interface HelloService {

    @GetMapping("/greeting")
    String hello();
}
