package com.fengxuechao.examples.auth.config.inmemory;

import com.fengxuechao.examples.auth.config.CustomTokenEnhancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.util.Base64Utils;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/8
 */
@Slf4j
@EnableAuthorizationServer
@Configuration
@Profile("inMemory")
public class AuthorizationServerConfigInMemory extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ApprovalStore approvalStore;

    @Autowired
    private TokenGranter tokenGranter;

    @Bean
    public TokenApprovalStore tokenApprovalStore() {
        TokenApprovalStore approvalStore = new TokenApprovalStore();
        approvalStore.setTokenStore(tokenStore);
        return approvalStore;
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        log.info("Authorization: Basic {}", Base64Utils.encodeToString("client:123456".getBytes("UTF-8")));
        clients.inMemory()
                .withClient("client")
                .secret("123456")
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(3600)
                .authorizedGrantTypes("client_credentials", "password", "authorization_code", "implicit", "refresh_token", "sms_code")
                .redirectUris("https://www.baidu.com")
                .scopes("read")
                .authorities("ROLE_USER")
                .additionalInformation("auth=true")
        ;
    }

    /**
     * 认证服务器节点配置
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenGranter(tokenGranter)
                .tokenStore(tokenStore)
                .approvalStore(approvalStore)
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
                .setClientDetailsService(clientDetailsService);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
}
