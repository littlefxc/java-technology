package com.fengxuechao.examples.security.authorization;

import com.fengxuechao.examples.security.domain.SPermission;
import com.fengxuechao.examples.security.domain.SRole;
import com.fengxuechao.examples.security.service.SecurityDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 权限资源管理器
 * 根据用户请求的地址，获取访问该地址需要的所需权限
 * @author fengxuechao
 * @date 2019-03-29
 */
@Slf4j
@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    SecurityDataService securityDataService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取请求起源路径
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        log.info("MyFilterInvocationSecurityMetadataSource getAttributes [requestUrl={}]", requestUrl);
        //登录页面就不需要权限
        if ("/login".equals(requestUrl)) {
            return null;
        }
        //用来存储访问路径需要的角色或权限信息
        List<String> tempPermissionList = new ArrayList<String>();
        //获取角色列表
        List<SRole> sRoleList = securityDataService.findSRoleListBySPermissionUrl(requestUrl);
        log.info("MyFilterInvocationSecurityMetadataSource getAttributes [sRoleList={}]", sRoleList);
        for(SRole sRole : sRoleList) {
            tempPermissionList.add(sRole.getRole());
        }
        //径获取资源权限列表
        List<SPermission> sPermissionList = securityDataService.findSPermissionListBySPermissionUrl(requestUrl);
        log.info("MyFilterInvocationSecurityMetadataSource getAttributes [sPermissionList={}]", sPermissionList);
        for(SPermission sPermission : sPermissionList) {
            tempPermissionList.add(sPermission.getPermission());
        }
        //如果没有权限控制的url，可以设置都可以访问，也可以设置默认不许访问
        if(tempPermissionList.isEmpty()) {
            return null;//都可以访问
            //tempPermissionList.add("DEFAULT_FORBIDDEN");//默认禁止
        }
        String[] permissionArray = tempPermissionList.toArray(new String[0]);
        String str = tempPermissionList.stream().reduce((s, s2) -> s = s + "," + s2).get();
        log.info("MyFilterInvocationSecurityMetadataSource getAttributes [permissionArray={}]", str);
        return SecurityConfig.createList(permissionArray);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
