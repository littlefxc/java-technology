# Spring Security Oauth2 令牌增加额外信息

在实现了 Oauth2 后，我想要在令牌增加中额外信息，那么该怎么做？

下面是我的做法，首先实现 `org.springframework.security.oauth2.provider.token.TokenEnhancer` 接口：

```java
package com.fengxuechao.examples.auth.config;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * token 额外信息
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/16
 */
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<String, Object>();
        additionalInfo.put("organization", authentication.getName() + randomAlphabetic(4));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
```

然后在 `AuthorizationServerConfigurerAdapter` 认证服务代码中配置：

```java
public class AuthorizationServerConfigInJwt extends AuthorizationServerConfigurerAdapter {
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
        converter.setKeyPair(new KeyStoreKeyFactory(resource, keyStorePass.toCharArray()).getKeyPair(keyPairAlias));
        // 使用对称密钥来签署令牌
        // converter.setSigningKey("fengxuechao.littlefxc");
        return converter;
    }
}
```

或者 `tokenServices.setTokenEnhancer(tokenEnhancer);`

最后演示一下最终效果：

```json
{
  "access_token": "4aae3856-bc33-4e4d-86bc-eb475fc45569",
  "token_type": "bearer",
  "refresh_token": "fe2ed35d-5c53-4610-abb7-c1053cba6803",
  "expires_in": 119,
  "scope": "read",
  "organization": "userAKqz"
}
```

最终返回的 Token 信息中多了一个属性 `organization`，结果符合期望结果。