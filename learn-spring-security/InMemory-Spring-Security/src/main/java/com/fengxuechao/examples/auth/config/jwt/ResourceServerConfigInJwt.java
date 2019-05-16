package com.fengxuechao.examples.auth.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;

/**
 * 资源服务器配置：我将认证服务器和资源服务器定义为完全分离且可独立部署，这也是我为什么两个配置类中有些相同的 bean 的原因
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/8
 */
@Slf4j
@EnableResourceServer
@Configuration
@Profile("jwt")
public class ResourceServerConfigInJwt extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(resourceServerTokenServices());
    }

    /**
     * 资源服务器的 TokenServices 配置
     *
     * @return
     */
    @Bean
    @Primary
    public ResourceServerTokenServices resourceServerTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
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

    @Value("classpath:public.txt")
    private Resource resource;

    /**
     * jwt token：使用了非对称密钥对来签署令牌:
     * 1.生成 JKS Java KeyStore 文件：keytool -genkeypair -alias mytest -keyalg RSA -keypass mypass -keystore mytest.jks -storepass mypass
     * 2.导出公钥：keytool -list -rfc --keystore mytest.jks | openssl x509 -inform pem -pubkey
     * 3.将 PUBLIC KEY 保存至 public.txt
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        String publicKey = null;
        try {
            publicKey = IOUtils.toString(resource.getInputStream(), "UTF-8");
        } catch (final IOException e) {
            log.error("资源服务器加载公钥文件'public.txt'失败", e);
        }
        converter.setVerifierKey(publicKey);
        return converter;
    }
}
