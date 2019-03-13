package com.littlefxc.examples.cloud.fallback;

import com.littlefxc.examples.cloud.ProviderService;
import org.springframework.stereotype.Component;

/**
 * @author fengxuechao
 * @date 2019/2/25
 **/
@Component
public class ProviderServiceFallback implements ProviderService {

    @Override
    public String get(String key) {
        return "get 请求熔断降级成功";
    }

    @Override
    public String post(String key, String value) {
        return "post 请求熔断降级成功";
    }

    @Override
    public String put(String key, String value) {
        return "put 请求熔断降级成功";
    }

    @Override
    public String delete(String key) {
        return "delete 请求熔断降级成功";
    }
}
