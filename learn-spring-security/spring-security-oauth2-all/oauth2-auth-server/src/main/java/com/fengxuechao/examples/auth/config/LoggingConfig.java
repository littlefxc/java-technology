package com.fengxuechao.examples.auth.config;

import ch.qos.logback.core.PropertyDefinerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * logback 动态配置：https://github.com/johnnian/Blog/issues/58
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/19
 */
@ConditionalOnProperty(prefix = "logging.kafka", name = "enabled", matchIfMissing = false)
@EnableConfigurationProperties(LoggingProperties.class)
@Configuration
public class LoggingConfig {

    @Autowired
    LoggingProperties properties;

    @Bean
    public PropertyDefinerBase producerConfig() {
        return new PropertyDefinerBase() {
            @Override
            public String getPropertyValue() {
                return properties.getProducerConfig();
            }
        };
    }
}
