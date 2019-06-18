# Spring Security Oauth2 添加自定义过滤器和oauth2认证后API权限控制

在搭建完 spring-security-oauth2 整个微服务框架后，来了一个需求：

> 每个微服务都需要对访问进行鉴权，每个微服务应用都需要明确当前访问用户和他的权限。

auth 系统的主要功能是授权认证和鉴权。

授权认证已经完成，那么如何对用户的访问进行鉴权呢？

首先需要明确什么时候发生鉴权？

鉴权发生在用户已经认证后携带了 access_token 信息但还没用访问到目标资源的时候。

知道了鉴权发生的时间，需要明白怎么鉴权？

我的想法是添加一个用于鉴权的过滤器，Spring Security 默认的过滤器链([官网](https://docs.spring.io/spring-security/site/docs/5.0.0.M1/reference/htmlsingle/#ns-custom-filters))：

| 别名 | 类名称 | Namespace Element or Attribute |
| --- | ------ | ------------------------------ |
| CHANNEL_FILTER | ChannelProcessingFilter | http/intercept-url@requires-channel |
| SECURITY_CONTEXT_FILTER | SecurityContextPersistenceFilter | http |
| CONCURRENT_SESSION_FILTER | ConcurrentSessionFilter | session-management/concurrency-control |
| HEADERS_FILTER | HeaderWriterFilter | http/headers |
| CSRF_FILTER | CsrfFilter | http/csrf |
| LOGOUT_FILTER | LogoutFilter | http/logout |
| X509_FILTER | X509AuthenticationFilter | http/x509 |
| PRE_AUTH_FILTER | AbstractPreAuthenticatedProcessingFilter( Subclasses) | N/A |
| CAS_FILTER | CasAuthenticationFilter	N/A |
| FORM_LOGIN_FILTER | UsernamePasswordAuthenticationFilter | http/form-login |
| BASIC_AUTH_FILTER | BasicAuthenticationFilter | http/http-basic |
| SERVLET_API_SUPPORT_FILTER | SecurityContextHolderAwareRequestFilter | http/@servlet-api-provision |
| JAAS_API_SUPPORT_FILTER | JaasApiIntegrationFilter | http/@jaas-api-provision |
| REMEMBER_ME_FILTER | RememberMeAuthenticationFilter | http/remember-me |
| ANONYMOUS_FILTER | AnonymousAuthenticationFilter | http/anonymous |
| SESSION_MANAGEMENT_FILTER | SessionManagementFilter | session-management |
| EXCEPTION_TRANSLATION_FILTER | ExceptionTranslationFilter | http |
| FILTER_SECURITY_INTERCEPTOR | FilterSecurityInterceptor | http |
| SWITCH_USER_FILTER | SwitchUserFilter | N/A |

> 过滤器顺序从上到下

`FilterSecurityInterceptor` 是 filterchain 中比较复杂，也是比较核心的过滤器，主要负责web应用安全授权的工作。

我想添加的过滤器是添加在 `FilterSecurityInterceptor` 之后。

`Oauth2FilterSecurityInterceptor` 是模仿 FilterSecurityInterceptor 实现，继承 AbstractSecurityInterceptor 和实现 Filter 接口。

整个过程需要依赖 AuthenticationManager、AccessDecisionManager 和 FilterInvocationSecurityMetadataSource。

- AuthenticationManager是认证管理器，实现用户认证的入口；
- AccessDecisionManager是访问决策器，决定某个用户具有的角色，是否有足够的权限去访问某个资源；
- FilterInvocationSecurityMetadataSource是资源源数据定义，即定义某一资源可以被哪些角色访问。

## 自定义鉴权过滤器 `Oauth2FilterSecurityInterceptor` 的实现

```java
package com.fengxuechao.examples.auth.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import java.io.IOException;

/**
 * 比较核心的过滤器: 主要负责web应用鉴权的工作。
 * 需要依赖:
 * - AuthenticationManager:认证管理器，实现用户认证的入口;
 * - AccessDecisionManager:访问决策器，决定某个用户具有的角色，是否有足够的权限去访问某个资源;
 * - FilterInvocationSecurityMetadataSource:资源源数据定义，即定义某一资源可以被哪些角色访问.
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/17
 */
@Slf4j
public class Oauth2FilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private Oauth2FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (log.isInfoEnabled()) {
            log.info("Oauth2FilterSecurityInterceptor init");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (log.isInfoEnabled()) {
            log.info("Oauth2FilterSecurityInterceptor doFilter");
        }
        FilterInvocation filterInvocation = new FilterInvocation(request, response, chain);
        invoke(filterInvocation);
    }

    public void invoke(FilterInvocation filterInvocation) throws IOException, ServletException {
        // filterInvocation里面有一个被拦截的url
        // 里面调用 Oauth2AccessDecisionManager 的 getAttributes(Object object) 这个方法获取 filterInvocation 对应的所有权限
        // 再调用 Oauth2AccessDecisionManager 的 decide方法来校验用户的权限是否足够
        InterceptorStatusToken interceptorStatusToken = super.beforeInvocation(filterInvocation);
        try {
            // 执行下一个拦截器
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
        } finally {
            super.afterInvocation(interceptorStatusToken, null);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    /**
     * 资源源数据定义，设置为自定义的 SecureResourceFilterInvocationDefinitionSource
     *
     * @return
     */
    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return securityMetadataSource;
    }

    public void setOauth2AccessDecisionManager(Oauth2AccessDecisionManager accessDecisionManager) {
        super.setAccessDecisionManager(accessDecisionManager);
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    public void setSecurityMetadataSource(Oauth2FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }
}
```

看下父类的 `beforeInvocation` 方法，其中省略了一些不重要的代码片段:

```java
public abstract class AbstractSecurityInterceptor implements InitializingBean, ApplicationEventPublisherAware, MessageSourceAware {
   protected InterceptorStatusToken beforeInvocation(Object object) {
		// 代码省略
        
        // 根据 SecurityMetadataSource 获取配置的权限属性
		Collection<ConfigAttribute> attributes = this.obtainSecurityMetadataSource()
				.getAttributes(object);

		// 代码省略

        // 判断是否需要对认证实体重新认证，默认为否
		Authentication authenticated = authenticateIfRequired();

		// Attempt authorization
		try {
			// 决策管理器开始决定是否授权，如果授权失败，直接抛出 AccessDeniedException
	        this.accessDecisionManager.decide(authenticated, object, attributes);
		}
		catch (AccessDeniedException accessDeniedException) {
			publishEvent(new AuthorizationFailureEvent(object, attributes, authenticated,
					accessDeniedException));

			throw accessDeniedException;
		}

		// 代码省略
   }
}
```
    
## 自定义资源源数据定义 Oauth2FilterInvocationSecurityMetadataSource

```java
package com.fengxuechao.examples.auth.authorization;

import com.fengxuechao.examples.auth.service.UserRolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 资源源数据定义，即定义某一资源可以被哪些角色访问
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/14
 */
@Slf4j
@Component
public class Oauth2FilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {

    private UserRolePermissionService service;

    public Oauth2FilterInvocationSecurityMetadataSource(UserRolePermissionService service) {
        this.service = service;
    }

    
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if ("/user/profile".equals(((FilterInvocation) object).getRequestUrl())) {
            // [/user/profile] 不需要鉴权
            return null;
        }
        /*if (object instanceof FilterInvocation) {
            FilterInvocation fi = (FilterInvocation) object;
            String requestUrl = fi.getRequestUrl();
            // 返回请求所需的权限
            List<Role> roleList = service.findRoleListByPermissionUrl(requestUrl);
            String[] roleArray = new String[roleList.size()];
            roleArray = roleList.toArray(roleArray);
            return SecurityConfig.createList(roleArray);
        }
        return Collections.EMPTY_LIST;*/
        return SecurityConfig.createList("ROLE_ADMIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
```

为了调试的方便，直接定死任何访问请求都需要管理员权限(/user/profile 除外)，调试通过后，再往里面添加业务逻辑代码。

## 自定义决策管理器 Oauth2AccessDecisionManager

```java
package com.fengxuechao.examples.auth.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * 访问决策器，决定某个用户具有的角色，是否有足够的权限去访问某个资源
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/14
 */
@Slf4j
@Component
public class Oauth2AccessDecisionManager implements AccessDecisionManager {

    /**
     * @param authentication   用户凭证
     * @param resource         资源 URL
     * @param configAttributes 资源 URL 所需要的权限
     * @throws AccessDeniedException               资源拒绝访问
     * @throws InsufficientAuthenticationException 用户凭证不符
     */
    @Override
    public void decide(Authentication authentication, Object resource, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        log.info("[决策管理器]:开始判断请求 {} 需要的权限", ((FilterInvocation) resource).getRequestUrl());
        if (configAttributes == null || configAttributes.isEmpty()) {
            log.info("[决策管理器]:请求 {} 无需权限", ((FilterInvocation) resource).getRequestUrl());
            return;
        }
        log.info("[决策管理器]:请求 {} 需要的权限 - {}", ((FilterInvocation) resource).getRequestUrl(), configAttributes);
        // 判断用户所拥有的权限，是否符合对应的Url权限，用户权限是实现 UserDetailsService#loadUserByUsername 返回用户所对应的权限
        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        log.info("[决策管理器]:用户 {} 拥有的权限 - {}", authentication.getName(), authentication.getAuthorities());
        while (ite.hasNext()) {
            ConfigAttribute neededAuthority = ite.next();
            String neededAuthorityStr = neededAuthority.getAttribute();
            for (GrantedAuthority existingAuthority : authentication.getAuthorities()) {
                if (neededAuthorityStr.equals(existingAuthority.getAuthority())) {
                    return;
                }
            }
        }
        log.info("[决策管理器]:用户 {} 没有访问资源 {} 的权限!", authentication.getName(), ((FilterInvocation) resource).getRequestUrl());
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    /**
     * 是否支持 FilterInvocationSecurityMetadataSource 需要将这里的false改为true
     *
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
```

## 配置自定义鉴权过滤器 `Oauth2FilterSecurityInterceptor` 在 Spring Security 过滤器链中的位置

```java
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
```

## 配置用户权限

```java
package com.fengxuechao.examples.auth.userdetails;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/15
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, "123456", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
```

## 演示结果

### 用户拥有资源所需权限

请求：

```http request
GET http://localhost:8080/order/1

HTTP/1.1 200 
X-Application-Context: application:inMemory
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: text/plain;charset=UTF-8
Content-Length: 12
Date: Tue, 18 Jun 2019 01:50:48 GMT

order id : 1

Response code: 200; Time: 57ms; Content length: 12 bytes
```

日志：

```log
2019-06-18 09:50:48.955  INFO 5288 --- [nio-8080-exec-3] .f.e.a.a.Oauth2FilterSecurityInterceptor : Oauth2FilterSecurityInterceptor doFilter
2019-06-18 09:50:48.955 DEBUG 5288 --- [nio-8080-exec-3] .f.e.a.a.Oauth2FilterSecurityInterceptor : Secure object: FilterInvocation: URL: /order/1; Attributes: [ROLE_USER]
2019-06-18 09:50:48.956 DEBUG 5288 --- [nio-8080-exec-3] .f.e.a.a.Oauth2FilterSecurityInterceptor : Previously Authenticated: org.springframework.security.oauth2.provider.OAuth2Authentication@f5aeefea: Principal: org.springframework.security.core.userdetails.User@36ebcb: Username: user; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; credentialsNonExpired: true; AccountNonLocked: true; Granted Authorities: ROLE_USER; Credentials: [PROTECTED]; Authenticated: true; Details: remoteAddress=127.0.0.1, tokenType=bearertokenValue=<TOKEN>; Granted Authorities: ROLE_USER
2019-06-18 09:50:48.956  INFO 5288 --- [nio-8080-exec-3] c.f.e.a.a.Oauth2AccessDecisionManager    : [决策管理器]:开始判断请求 /order/1 需要的权限
2019-06-18 09:50:48.956  INFO 5288 --- [nio-8080-exec-3] c.f.e.a.a.Oauth2AccessDecisionManager    : [决策管理器]:请求 /order/1 需要的权限 - [ROLE_USER]
2019-06-18 09:50:48.956  INFO 5288 --- [nio-8080-exec-3] c.f.e.a.a.Oauth2AccessDecisionManager    : [决策管理器]:用户 user 拥有的权限 - [ROLE_USER]
2019-06-18 09:50:48.956 DEBUG 5288 --- [nio-8080-exec-3] .f.e.a.a.Oauth2FilterSecurityInterceptor : Authorization successful
2019-06-18 09:50:48.957 DEBUG 5288 --- [nio-8080-exec-3] .f.e.a.a.Oauth2FilterSecurityInterceptor : RunAsManager did not change Authentication object
```

### 用户没有资源所需权限

请求：

```http request
GET http://localhost:8080/order/1

HTTP/1.1 403 
Cache-Control: no-store
Pragma: no-cache
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Tue, 18 Jun 2019 01:44:49 GMT

{
  "error": "access_denied",
  "error_description": "权限不足!"
}

Response code: 403; Time: 35ms; Content length: 53 bytes
```

日志：

```log
2019-06-18 09:44:44.684  INFO 10624 --- [nio-8080-exec-2] .f.e.a.a.Oauth2FilterSecurityInterceptor : Oauth2FilterSecurityInterceptor doFilter
2019-06-18 09:44:44.685 DEBUG 10624 --- [nio-8080-exec-2] .f.e.a.a.Oauth2FilterSecurityInterceptor : Public object - authentication not attempted
2019-06-18 09:44:49.448  INFO 10624 --- [nio-8080-exec-6] .f.e.a.a.Oauth2FilterSecurityInterceptor : Oauth2FilterSecurityInterceptor doFilter
2019-06-18 09:44:49.449 DEBUG 10624 --- [nio-8080-exec-6] .f.e.a.a.Oauth2FilterSecurityInterceptor : Secure object: FilterInvocation: URL: /order/1; Attributes: [ROLE_ADMIN]
2019-06-18 09:44:49.449 DEBUG 10624 --- [nio-8080-exec-6] .f.e.a.a.Oauth2FilterSecurityInterceptor : Previously Authenticated: org.springframework.security.oauth2.provider.OAuth2Authentication@22d262ad: Principal: org.springframework.security.core.userdetails.User@36ebcb: Username: user; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; credentialsNonExpired: true; AccountNonLocked: true; Granted Authorities: ROLE_USER; Credentials: [PROTECTED]; Authenticated: true; Details: remoteAddress=127.0.0.1, tokenType=bearertokenValue=<TOKEN>; Granted Authorities: ROLE_USER
2019-06-18 09:44:49.450  INFO 10624 --- [nio-8080-exec-6] c.f.e.a.a.Oauth2AccessDecisionManager    : [决策管理器]:开始判断请求 /order/1 需要的权限
2019-06-18 09:44:49.450  INFO 10624 --- [nio-8080-exec-6] c.f.e.a.a.Oauth2AccessDecisionManager    : [决策管理器]:请求 /order/1 需要的权限 - [ROLE_ADMIN]
2019-06-18 09:44:49.450  INFO 10624 --- [nio-8080-exec-6] c.f.e.a.a.Oauth2AccessDecisionManager    : [决策管理器]:用户 user 拥有的权限 - [ROLE_USER]
2019-06-18 09:44:49.451  INFO 10624 --- [nio-8080-exec-6] c.f.e.a.a.Oauth2AccessDecisionManager    : [决策管理器]:用户 user 没有访问资源 /order/1 的权限!
```

返回结果和日志符合期望结果

## 参考资源

[http://www.spring4all.com/article/422](http://www.spring4all.com/article/422)