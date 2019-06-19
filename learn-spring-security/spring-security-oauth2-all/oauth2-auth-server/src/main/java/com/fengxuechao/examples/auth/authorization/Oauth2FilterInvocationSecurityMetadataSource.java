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
