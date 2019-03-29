package com.fengxuechao.examples.sso.server.dao;

import com.fengxuechao.examples.sso.server.domain.SRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色信息查询 
 * @author Veiking 
 */
@Repository
@Mapper
public interface SRoleDao {  
    /** 
     * 根据用户ID获取角色列表 
     * @param sUserId 
     * @return 
     */  
    @Select(value=" SELECT sr.* FROM s_role sr " +
                    " LEFT JOIN s_user_role sur ON sr.id = sur.fk_role_id " +   
                    " LEFT JOIN s_user su ON sur.fk_user_id = su.id " +   
                    " WHERE su.id = #{sUserId} ")  
    public List<SRole> findSRoleListBySUserId(int sUserId);
  
    /** 
     * 根据资源路径获取角色列表 
     * @param sPermissionUrl 
     * @return 
     */  
    @Select(value=" SELECT sr.* FROM s_role sr " +   
                    " LEFT JOIN s_role_permission srp ON sr.id = srp.fk_role_id " +   
                    " LEFT JOIN s_permission sp ON srp.fk_permission_id = sp.id " +   
                    " WHERE sp.url = #{sPermissionUrl} ")  
    public List<SRole> findSRoleListBySPermissionUrl(String sPermissionUrl);  
}  