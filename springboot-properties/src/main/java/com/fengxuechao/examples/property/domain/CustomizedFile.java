package com.fengxuechao.examples.property.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 自定义配置文件, 需要配合使用后@Configuration和@PropertySource("classpath:customized-file.properties")来指定
 *
 * @author fengxuechao
 */
@Configuration
@ConfigurationProperties(prefix = "customizedFile")
@PropertySource("classpath:customized-file-${spring.profiles.active}.properties")
public class CustomizedFile {
    private String name;
    private String author;
    private String path;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CustomizedFile{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", path='" + path + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
