package com.littlefxc.examples.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author fengxuechao
 */
@SpringCloudApplication
@RestController
public class UserRibbonApplication {

    @Autowired
    RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(UserRibbonApplication.class, args);
    }

    @RequestMapping("/hi")
    public String hi(@RequestParam(value = "name", defaultValue = "Artaban") String name) {
        String greeting = greeting();
        return String.format("%s, %s!", greeting, name);
    }

    /**
     * ribbon 发出的请求
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "ribbonFallback")
    public String greeting() {
        return this.restTemplate.getForObject("http://say-hello/greeting", String.class);
    }

    /**
     * ribbon 断路器
     *
     * @return
     */
    public String ribbonFallback() {
        return "ribbon error";
    }

}