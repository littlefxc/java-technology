package com.fengxuechao.examples.sso.server.service;

import com.fengxuechao.examples.sso.server.dao.SPermissionDao;
import com.fengxuechao.examples.sso.server.dao.SRoleDao;
import com.fengxuechao.examples.sso.server.dao.SUserDao;
import com.fengxuechao.examples.sso.server.domain.SPermission;
import com.fengxuechao.examples.sso.server.domain.SRole;
import com.fengxuechao.examples.sso.server.domain.SUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Security 数据服务 
 *  
 * @author Veiking 
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