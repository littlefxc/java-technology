package com.example.atomikos.dao.one;

import com.example.atomikos.entity.ArticleDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date 2018/11/30
 * @author fengxuechao
 */
@Mapper
@Repository
public interface ArticleDao {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 每页记录数
     * @return
     */
    @Select("select id, title, content, url from article limit ${page}, ${size}")
    List<ArticleDO> list(@Param("page") Integer page, @Param("size") Integer size);

    /**
     * 根据主键插叙单条记录
     *
     * @param id
     * @return
     */
    @Select("select id, title, content, url from article where id = #{id}")
    ArticleDO get(Long id);

    /**
     * 添加一条记录
     *
     * @param article
     * @return 自增主键
     */
    @Insert("insert into article(title, url, content) values(#{title}, #{url}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Long save(ArticleDO article);

    /**
     * 修改一条记录
     * @param article 文章实体类
     * @return
     */
    @Update("update article set title = #{title}, content = #{content}, url = #{url} where id = #{id}")
    int update(ArticleDO article);

    /**
     * 删除一条记录
     * @param id 主键
     * @return
     */
    @Delete("delete from article where id = #{id}")
    int delete(Long id);

}
