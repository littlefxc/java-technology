package com.example.atomikos.service;

import com.example.atomikos.entity.BookDO;
import com.example.atomikos.entity.UserDO;

import java.util.List;

/**
 * 主要目的是测试分布式事务
 *
 * @author fengxuechao
 */
public interface BookService {

    /**
     * 保存
     *
     * @param book
     * @param user
     * @return
     */
    BookDO save(BookDO book, UserDO user);

    /**
     * 单条查询
     *
     * @param id
     * @return
     */
    BookDO get(Long id);

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    List<BookDO> list(Integer page, Integer size);

}
