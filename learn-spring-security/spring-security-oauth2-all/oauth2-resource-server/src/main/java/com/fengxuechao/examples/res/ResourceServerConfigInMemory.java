package com.fengxuechao.examples.res;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/16
 */
@EnableResourceServer
@Configuration
@Profile("inMemory")
public class ResourceServerConfigInMemory extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
    }

    /*@Autowired
    private ResourceServerProperties resource;

    @Primary
    @Bean
    public RemoteTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(resource.getUserInfoUri());
        if (resource.isPreferTokenInfo()){
            tokenServices.setCheckTokenEndpointUrl(resource.getTokenInfoUri());
        }
        tokenServices.setClientId(resource.getClientId());
        tokenServices.setClientSecret(resource.getClientSecret());
        return tokenServices;
    }*/
}

