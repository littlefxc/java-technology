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
