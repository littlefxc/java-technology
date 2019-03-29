package com.fengxuechao.examples.security.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 访问路径配置类
 * 可以理解成做简单访问过滤的，转发到相应的视图页面
 *
 * @author fengxuechao
 * @date 2019-03-29
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
    }
}  