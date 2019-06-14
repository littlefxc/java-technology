package com.fengxuechao.examples.auth.authorization;

import com.fengxuechao.examples.auth.domain.Role;
import com.fengxuechao.examples.auth.service.UserRolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 获取配置的权限属性:将请求的URL取出进行匹配事先设定的受限资源，最后返回需要的权限、角色。系统在启动的时候就会读取到配置的map集合，对于拦截到请求进行匹配
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/14
 */
@Slf4j
public class Oauth2ResourceFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {

    private UserRolePermissionService userRolePermissionService;

    public Oauth2ResourceFilterInvocationSecurityMetadataSource(UserRolePermissionService userRolePermissionService) {
        this.userRolePermissionService = userRolePermissionService;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (object instanceof FilterInvocation) {
            FilterInvocation filterInvocation = (FilterInvocation) object;
            String requestUrl = filterInvocation.getRequestUrl();
            List<Role> roleList = userRolePermissionService.findRoleListByPermissionUrl(requestUrl);
            String roleArray[] = new String[roleList.size()];
            roleArray = roleList.toArray(roleArray);
            return SecurityConfig.createList(roleArray);
        }
        return Collections.emptyList();
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
