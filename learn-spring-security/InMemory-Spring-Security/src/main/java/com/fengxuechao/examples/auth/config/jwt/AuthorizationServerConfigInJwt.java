package com.fengxuechao.examples.auth.config.jwt;

import com.fengxuechao.examples.auth.config.CustomTokenEnhancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.util.Base64Utils;

import java.util.Arrays;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/16
 */
@Slf4j
@EnableAuthorizationServer
@Configuration
@Profile("jwt")
public class AuthorizationServerConfigInJwt extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                // JWT 校验token时用的
                .tokenKeyAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        log.info("Authorization: Basic {}", Base64Utils.encodeToString("client:123456".getBytes("UTF-8")));
        clients.inMemory()
                .withClient("client")
                .secret("123456")
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(3600)
                .authorizedGrantTypes("client_credentials", "password", "authorization_code", "implicit", "refresh_token")
                .redirectUris("https://www.baidu.com")
                .scopes("read")
                .authorities("ROLE_USER")
                .additionalInformation("auth=true")
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // token 携带额外信息
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtTokenEnhancer()));
        endpoints
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
                .setClientDetailsService(clientDetailsService);
    }

    /**
     * Token 额外信息
     *
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    /**
     * token 仓库
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    /**
     * jwt token：使用了对称密钥来签署令牌
     *
     * @return
     */
    /*@Bean
    public.txt JwtAccessTokenConverter jwtTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("fengxuechao.littlefxc");
        return converter;
    }*/

    @Value("classpath:jwt_rsa.jks")
    private Resource resource;

    /**
     * jwt token：使用了非对称密钥对来签署令牌:
     * 1.生成 JKS Java KeyStore 文件：keytool -genkeypair -alias jwt_rsa -keyalg RSA -keypass 123456 -keystore jwt_rsa.jks -storepass 123456
     * 2.导出公钥：keytool -list -rfc --keystore jwt_rsa.jks | openssl x509 -inform pem -pubkey
     * 3.将 PUBLIC KEY 保存至 public.txt
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, "123456".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt_rsa"));
        return converter;
    }
}
