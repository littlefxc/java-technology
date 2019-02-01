package com.example.atomikos.dao.two;

import com.example.atomikos.entity.BookDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fengxuechao
 * @date 2018/11/30
 */
@Mapper
@Repository
public interface BookDao {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 每页记录数
     * @return
     */
    @Select("select id, name, article_id as articleId, user_id as userId from book limit ${page}, ${size}")
    List<BookDO> list(@Param("page") Integer page, @Param("size") Integer size);

    /**
     * 根据主键查询单条记录
     *
     * @param id
     * @return
     */
    @Select("select id, name, article_id as articleId, user_id as userId from book where id = #{id}")
    BookDO get(Long id);

    /**
     * 添加一条记录
     *
     * @param book
     * @return 自增主键
     */
    @Insert("insert into book(name, article_id, user_id) values(#{name}, #{articleId}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(BookDO book);

    /**
     * 修改一条记录
     *
     * @param book
     * @return
     */
    @Update("update book set name = #{name}, article_id = #{articleId}, user_id = #{userId} where id = #{id}")
    int update(BookDO book);

    /**
     * 删除一条记录
     *
     * @param id 主键
     * @return
     */
    @Delete("delete from book where id = #{id}")
    int delete(Long id);
}
