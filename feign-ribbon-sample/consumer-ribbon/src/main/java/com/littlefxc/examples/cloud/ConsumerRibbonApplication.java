package com.littlefxc.examples.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author fengxuechao
 */
@SpringCloudApplication// 包含注解@SpringBootApplication @EnableDiscoveryClient @EnableCircuitBreaker
@RestController
public class ConsumerRibbonApplication {

    @Autowired
    RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ConsumerRibbonApplication.class, args);
    }

    /**
     * get 请求
     *
     * @param key
     * @return
     */
    @GetMapping(value = "/get")
    @HystrixCommand(fallbackMethod = "getFallback")
    public String get(@RequestParam String key) {
        String url = "http://provider/get?key=" + key;// 域名部分使用服务命取代
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * post 请求
     *
     * @param key
     * @param value
     * @return
     */
    @PostMapping(value = "/post")
    @HystrixCommand(fallbackMethod = "postFallback")
    public String post(@RequestParam String key, @RequestParam String value) {
        String url = "http://provider/post?key={1}&value={2}";
        return restTemplate.postForObject(url, null, String.class, key, value);
    }

    /**
     * put 请求
     * @param key
     * @param value
     * @return
     */
    @PutMapping(value = "/put")
    @HystrixCommand(fallbackMethod = "putFallback")
    public String put(@RequestParam String key, @RequestParam String value) {
        String url = "http://provider/put?key=" + key + "&value=" + value;
        restTemplate.put(url, null); //
        return "put 请求测试成功";
    }

    /**
     * delete 请求
     * @param key
     * @return
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    @HystrixCommand(fallbackMethod = "deleteFallback")
    public String delete(@RequestParam("key") String key) {
        String url = "http://provider/delete?key={1}";
        restTemplate.delete(url, key);
        return "delete 请求测试成功";
    }

    /**
     * ribbon 断路器
     *
     * @return
     */
    public String getFallback(String key) {
        return "get 请求熔断降级成功";
    }

    public String postFallback(String key, String value) {
        return "post 请求熔断降级成功";
    }

    public String putFallback(String key, String value) {
        return "put 请求熔断降级成功";
    }

    public String deleteFallback(String key) {
        return "delete 请求熔断降级成功";
    }

}