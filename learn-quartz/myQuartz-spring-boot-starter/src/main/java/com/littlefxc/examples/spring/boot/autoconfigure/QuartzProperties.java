package com.littlefxc.examples.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengxuechao
 * @date 2019/1/2
 **/
@ConfigurationProperties("spring.quartz")
public class QuartzProperties {

    /**
     * Quartz job store type.
     */
    private JobStoreType jobStoreType = JobStoreType.MEMORY;

    /**
     * Additional Quartz Scheduler properties.
     */
    private final Map<String, String> properties = new HashMap<>();

    public JobStoreType getJobStoreType() {
        return this.jobStoreType;
    }

    public void setJobStoreType(JobStoreType jobStoreType) {
        this.jobStoreType = jobStoreType;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }
}
