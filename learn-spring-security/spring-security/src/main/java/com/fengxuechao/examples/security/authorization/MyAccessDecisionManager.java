package com.fengxuechao.examples.security.authorization;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 权限管理判断器|校验用户是否有权限访问请求资源
 * @author fengxuechao
 * @date 2019-03-29
 */
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        //当前用户所具有的权限
        Collection<? extends GrantedAuthority> userAuthorityList = authentication.getAuthorities();
        //访问资源所需的权限信息
        Collection<ConfigAttribute> needAuthoritieList = configAttributes;
        //依次循环对比，发现有匹配的即返回
        for(ConfigAttribute needAuthoritie: needAuthoritieList) {
            String needAuthoritieStr = needAuthoritie.getAttribute();
            for (GrantedAuthority userAuthority : userAuthorityList) {
                String userAuthorityStr = userAuthority.getAuthority();
                if (needAuthoritieStr.equals(userAuthorityStr)) {
                    return;
                }
            }
        }
        //执行到这里说明没有匹配到应有权限
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
