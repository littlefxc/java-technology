package com.littlefxc.spring.security.config;

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import static com.littlefxc.spring.security.config.SecurityConfig.REALM_NAME;

public class CustomAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(REALM_NAME);
        super.afterPropertiesSet();
    }
}