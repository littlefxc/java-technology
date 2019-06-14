package com.fengxuechao.examples.auth.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * 决策管理器:将请求所具有的权限与设定的受限资源所需的进行匹配，如果具有则返回，否则抛出没有正确的权限异常
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/14
 */
@Slf4j
public class Oauth2AccessDecisionManager implements AccessDecisionManager {

    /**
     * @param authentication   用户的权限
     * @param resource           资源url
     * @param configAttributes 所需要的权限
     * @throws AccessDeniedException               资源拒绝访问
     * @throws InsufficientAuthenticationException 用户凭证不符
     */
    @Override
    public void decide(Authentication authentication, Object resource, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if (log.isInfoEnabled()) {
            log.info("[决策管理器]:判断资源 {} 需要的权限", resource);
        }
        if (configAttributes == null || configAttributes.isEmpty()) {
            return;
        }

        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        // 判断用户所拥有的权限，是否符合对应的Url权限，用户权限是实现 UserDetailsService#loadUserByUsername 返回用户所对应的权限
        while (ite.hasNext()) {
            ConfigAttribute neededAuthority = ite.next();
            String neededAuthorityStr = neededAuthority.getAttribute();
            for (GrantedAuthority existingAuthority : authentication.getAuthorities()) {
                log.info("GrantedAuthority: {}", existingAuthority);
                if (neededAuthorityStr.equals(existingAuthority.getAuthority())) {
                    return;
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("[决策管理器]:用户 {} 没有访问资源 {} 的权限！", authentication.getPrincipal(), resource);
        }
        throw new AccessDeniedException("权限不足！");
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
