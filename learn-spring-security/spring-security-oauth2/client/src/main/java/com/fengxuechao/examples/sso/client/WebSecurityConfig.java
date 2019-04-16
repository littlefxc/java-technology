package com.fengxuechao.examples.sso.client;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 配置最核心的部分是 @EnableOAuth2Sso注解来开启SSO。
 * 这里要注意，我们需要重写WebSecurityConfigurerAdapter 否则所有的路径都会受到SSO的保护，
 * 这样无论用户访问哪个页面都会被重定向到登录页面，在这个例子里，index和login页面是唯一不需要被防护的。
 *
 * @author fengxuechao
 * @date 2019/3/27
 */
//@EnableOAuth2Sso
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**")
                .permitAll()
                .anyRequest()
                .authenticated();
        http.csrf().disable();
    }
}