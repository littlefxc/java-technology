package com.fengxuechao.examples.sso.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * 使用 @EnableOAuth2Client 注解来开启 client_credentials。
 * 这里要注意的是要明确在配置文件中配置 security.oauth2.client.grant-type=client_credentials 。
 * 同时允许要调用的接口，注意对比与 WebSecurityConfig 类的不同点。
 *
 * @author fengxuechao
 * @date 2019/3/27
 */
@EnableOAuth2Client
@EnableWebSecurity
public class WebSecurityConfig2 extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/remoteCall")
                .permitAll()
                .anyRequest()
                .authenticated();
        http.csrf().disable();
    }
}