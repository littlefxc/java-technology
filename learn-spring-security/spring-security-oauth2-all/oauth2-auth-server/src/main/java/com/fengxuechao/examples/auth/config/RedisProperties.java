package com.fengxuechao.examples.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/21
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Data
public class RedisProperties {

    private String nodes;

    private Integer commandTimeout;

    private Integer maxAttempts;

    private Integer maxRedirects;

    private Integer maxActive;

    private Integer maxWait;

    private Integer maxIdle;

    private Integer minIdle;

    private boolean testOnBorrow;

}
