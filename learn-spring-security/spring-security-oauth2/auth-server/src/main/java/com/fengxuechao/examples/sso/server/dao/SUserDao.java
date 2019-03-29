package com.fengxuechao.examples.sso.server.dao;

import com.fengxuechao.examples.sso.server.domain.SUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 用户信息查询 
 * @author Veiking 
 */
@Repository
@Mapper
public interface SUserDao {  
    /** 
     * 根据用户名获取用户 
     *  
     * @param name 
     * @return 
     */  
    @Select(value = " SELECT su.* FROM s_user su WHERE su.name = #{name} ")
    public SUser findSUserByName(String name);
  
}  