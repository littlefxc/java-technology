package com.fengxuechao.examples.sso.gate;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * 安全配置 @EnableWebSecurity 启用web安全
 *
 * @author fengxuechao
 * @date 2019/4/1
 */
@Configuration
@EnableOAuth2Client
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * http安全配置
     *
     * @param http http安全对象
     * @throws Exception http安全异常信息
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();  // 禁用csrf
    }

    @LoadBalanced
    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(
            OAuth2ClientContext oAuth2ClientContext, OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oAuth2ClientContext);
    }

    /**
     * 资源过滤器
     *
     * @return 资源过滤器
     */
    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }
}
