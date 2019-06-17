package com.fengxuechao.examples.auth.config;

import com.fengxuechao.examples.auth.authorization.Oauth2AccessDecisionManager;
import com.fengxuechao.examples.auth.authorization.Oauth2FilterInvocationSecurityMetadataSource;
import com.fengxuechao.examples.auth.authorization.Oauth2FilterSecurityInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/8
 */
@Slf4j
@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    AuthenticationManager manager;

    @Autowired
    Oauth2AccessDecisionManager accessDecisionManager;

    @Autowired
    Oauth2FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterAfter(createApiAuthenticationFilter(), FilterSecurityInterceptor.class);
    }

    /**
     * API权限控制
     * 过滤器优先度在 FilterSecurityInterceptor 之后
     * spring-security 的默认过滤器列表见 https://docs.spring.io/spring-security/site/docs/5.0.0.M1/reference/htmlsingle/#ns-custom-filters
     *
     * @return
     */
    private Oauth2FilterSecurityInterceptor createApiAuthenticationFilter() {
        Oauth2FilterSecurityInterceptor interceptor = new Oauth2FilterSecurityInterceptor();
        interceptor.setAuthenticationManager(manager);
        interceptor.setAccessDecisionManager(accessDecisionManager);
        interceptor.setSecurityMetadataSource(securityMetadataSource);
        return interceptor;
    }
}
