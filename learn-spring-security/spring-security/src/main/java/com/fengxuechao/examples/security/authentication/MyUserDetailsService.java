package com.fengxuechao.examples.security.authentication;

import com.fengxuechao.examples.security.domain.SPermission;
import com.fengxuechao.examples.security.domain.SRole;
import com.fengxuechao.examples.security.domain.SUser;
import com.fengxuechao.examples.security.service.SecurityDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提供用户信息封装服务 
 * @author fengxuechao
 * @date 2019-03-29
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private SecurityDataService securityDataService;

    /**
     * 根据用户输入的用户名返回数据源中用户信息的封装，返回一个UserDetails 
     */  
    @Override  
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SUser sUser = securityDataService.findSUserByName(username);
        //用户角色列表  
        List<SRole> sRoleList = securityDataService.findSRoleListBySUserId(sUser.getId());
        //用户资源权限列表  
        List<SPermission> sPermissionList = securityDataService.findSPermissionListBySUserId(sUser.getId());
        return new MyUserDetails(sUser, sRoleList, sPermissionList);
    }  
  
}  