package com.fengxuechao.examples.security.service;

import com.fengxuechao.examples.security.dao.SPermissionDao;
import com.fengxuechao.examples.security.dao.SRoleDao;
import com.fengxuechao.examples.security.dao.SUserDao;
import com.fengxuechao.examples.security.domain.SPermission;
import com.fengxuechao.examples.security.domain.SRole;
import com.fengxuechao.examples.security.domain.SUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Security 数据服务 
 * @author fengxuechao
 * @date 2019-03-29
 */  
@Service
public class SecurityDataService {  
    @Autowired
    private SUserDao sUserDao;
    @Autowired  
    private SRoleDao sRoleDao;
    @Autowired  
    private SPermissionDao sPermissionDao;
  
    public SUser findSUserByName(String name) {
        return sUserDao.findSUserByName(name);  
    }  
  
    public List<SRole> findSRoleListBySUserId(int sUserId) {
        return sRoleDao.findSRoleListBySUserId(sUserId);  
    }  
  
    public List<SRole> findSRoleListBySPermissionUrl(String sPermissionUrl) {  
        return sRoleDao.findSRoleListBySPermissionUrl(sPermissionUrl);  
    }  
  
    public List<SPermission> findSPermissionListBySUserId(int sUserId) {
        return sPermissionDao.findSPermissionListBySUserId(sUserId);  
    }  
  
    public List<SPermission> findSPermissionListBySPermissionUrl(String sPermissionUrl) {  
        return sPermissionDao.findSPermissionListBySPermissionUrl(sPermissionUrl);  
    }  
}  