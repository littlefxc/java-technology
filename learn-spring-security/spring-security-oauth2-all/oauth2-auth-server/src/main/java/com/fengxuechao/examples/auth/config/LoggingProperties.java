package com.fengxuechao.examples.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/19
 */
@Data
@ConfigurationProperties(prefix = "logging.kafka")
public class LoggingProperties {

    private Boolean enabled;

    private String logHome;

    private String pattern;

    private String producerConfig;
}
