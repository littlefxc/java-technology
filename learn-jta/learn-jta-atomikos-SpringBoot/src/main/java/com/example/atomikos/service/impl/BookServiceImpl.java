package com.example.atomikos.service.impl;

import com.example.atomikos.dao.one.UserDao;
import com.example.atomikos.dao.two.BookDao;
import com.example.atomikos.entity.BookDO;
import com.example.atomikos.entity.UserDO;
import com.example.atomikos.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fengxuechao
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private UserDao userDao;

    /**
     * 保存书本和文章, 使用声明式事务(tx+aop形式)
     *
     * @param book {@link BookDO}
     * @param user {@link UserDO}
     * @return
     */
    @Override
    public BookDO save(BookDO book, UserDO user) {
        int userSave = userDao.save(user);
        if (userSave == 0) {
            return null;
        }
        book.setUserId(user.getId());
        int bookSave = bookDao.save(book);
        if (bookSave == 0) {
            return null;
        }
//        throw new RuntimeException("测试分布式事务(tx+aop形式)");
        return book;
    }

    /**
     * 单条查询
     *
     * @param id
     * @return
     */
    @Override
    public BookDO get(Long id) {
        BookDO book = bookDao.get(id);
        UserDO user = userDao.get(book.getUserId());
        return new BookDO(book.getId(), book.getName(), book.getArticleId(), user.getId());
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<BookDO> list(Integer page, Integer size) {
        page = (page < 1 ? 0 : page - 1) * size;
        return bookDao.list(page, size);
    }

    /**
     * 修改书本和文章, 使用声明式事务(注解形式)
     *
     * @param book
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BookDO update(BookDO book, UserDO user) {
        int bookUpdate = bookDao.update(book);
        if (bookUpdate != 1) {
            return null;
        }
        int userUpdate = userDao.update(user);
        if (userUpdate != 1) {
            return null;
        }
        throw new RuntimeException("测试分布式事务(注解形式)");
//        return book;
    }

    /**
     * 删除书本和文章
     *
     * @param id
     * @return
     */
    public int delete(Long id) {
        BookDO book = bookDao.get(id);
        System.err.println(book);
        Long userId = book.getUserId();
        int userDelete = userDao.delete(userId);
        if (userDelete != 1) {
            return 0;
        }
        int bookDelete = bookDao.delete(id);
        if (bookDelete != 1) {
            return 0;
        }
//        throw new RuntimeException("测试没有添加分布式事务管理)");
        return 1;
    }

}
