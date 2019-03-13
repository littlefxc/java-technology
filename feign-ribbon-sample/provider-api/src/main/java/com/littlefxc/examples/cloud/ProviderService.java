package com.littlefxc.examples.cloud;

import com.littlefxc.examples.cloud.fallback.ProviderServiceFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 使用@FeignClient注解来指定这个接口所要调用的服务名称
 * 使用Spring MVC注解来定义服务接口
 * @author fengxuechao
 * @date 2019/2/25
 **/
@FeignClient(value = "provider", fallback = ProviderServiceFallback.class)
public interface ProviderService {

    @GetMapping(value = "/get")
    public String get(@RequestParam("key") String key);

    @PostMapping(value = "/post")
    public String post(@RequestParam("key") String key, @RequestParam("value") String value);

    @PutMapping(value = "/put")
    public String put(@RequestParam("key") String key, @RequestParam("value") String value);

    @DeleteMapping(value = "/delete")
    public String delete(@RequestParam("key") String key);
}
