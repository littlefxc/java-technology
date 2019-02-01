package com.example.atomikos.dao.one;

import com.example.atomikos.entity.UserDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fengxuechao
 * @date 2018/11/30
 */
@Repository
public interface UserDao {

    /**
     * 根据主键查询一条记录
     *
     * @param id
     * @return
     */
    @Select("select id, username, password from user where id = #{id}")
    UserDO get(Long id);

    /**
     * 分页列表查询
     *
     * @param page
     * @param size
     * @return
     */
    @Select("select id, username, password from user limit #{page}, #{size}")
    List<UserDO> list(Integer page, Integer size);

    /**
     * 保存
     *
     * @param userDO
     * @return 自增主键
     */
    @Insert("insert into user(username, password) values(#{username}, #{password})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int save(UserDO userDO);

    /**
     * 修改一条记录
     *
     * @param user
     * @return
     */
    @Update("update user set username = #{username}, password = #{password} where id = #{id}")
    int update(UserDO user);

    /**
     * 删除一条记录
     *
     * @param id 主键
     * @return
     */
    @Delete("delete from user where id = #{id}")
    int delete(Long id);
}
