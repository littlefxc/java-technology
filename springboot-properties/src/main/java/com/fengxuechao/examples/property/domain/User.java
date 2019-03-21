package com.fengxuechao.examples.property.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

/**
 * 通过@ConfigurationProperties加载properties文件内的配置，通过prefix属性指定properties的配置的前缀
 */
@ConfigurationProperties(prefix = "user")
public class User {

    private String name;
    private Integer age;
    private UUID uuid;
    private Long longNum;
    private String secret;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getLongNum() {
        return longNum;
    }

    public void setLongNum(Long longNum) {
        this.longNum = longNum;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", uuid=" + uuid.toString() +
                ", longNum=" + longNum +
                ", secret='" + secret + '\'' +
                '}';
    }
}
