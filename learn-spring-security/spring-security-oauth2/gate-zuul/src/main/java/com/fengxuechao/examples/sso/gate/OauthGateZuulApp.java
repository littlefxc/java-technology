package com.fengxuechao.examples.sso.gate;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.security.oauth2.client.OAuth2LoadBalancerClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author fengxuechao
 * @date 2019/4/1
 */
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class OauthGateZuulApp {

    public static void main(String[] args) {
        SpringApplication.run(OauthGateZuulApp.class, args);
    }

    @RequestMapping("/test")
    public String test() {
        restTemplate.getAccessToken();
        return "test";
    }

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Bean
    public OAuth2RestTemplate restTemplate() {
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource());
        restTemplate.setAccessTokenProvider(new ResourceOwnerPasswordAccessTokenProvider());
        return restTemplate;
    }

    private OAuth2ProtectedResourceDetails resource() {

        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setAccessTokenUri("http://localhost:8081/oauth/token");

        resource.setClientId("client_1");
        resource.setClientSecret("123456");

        resource.setGrantType("password");
        resource.setScope(Arrays.asList("all"));

        resource.setUsername("admin");
        resource.setPassword("admin");

        return resource;
    }
}
