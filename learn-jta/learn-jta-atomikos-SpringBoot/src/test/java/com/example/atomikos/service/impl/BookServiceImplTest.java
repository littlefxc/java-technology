package com.example.atomikos.service.impl;

import com.example.atomikos.entity.BookDO;
import com.example.atomikos.entity.UserDO;
import com.example.atomikos.service.BookService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试分布式事务：切面拦截形式, 注解式, 编程式
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceImplTest {

    @Autowired
    BookService bookService;

    /**
     * 测试分布式事务(切面拦截形式)
     */
    @Test
    public void save() {
        BookDO book = new BookDO();
        book.setName("Book Name - 001");
        book.setArticleId(69L);

        UserDO user = new UserDO();
        user.setUsername("username - 001");
        user.setPassword("password - 001");
        BookDO bookDO = bookService.save(book, user);
        System.out.println(bookDO);
    }

    /**
     * 测试分布式事务(注解式)
     */
    @Test
    public void update() {
        BookDO book = new BookDO();
        book.setId(10L);
        book.setName("Book Name - 002");
        book.setArticleId(69L);

        UserDO user = new UserDO();
        user.setId(18L);
        user.setUsername("username - 002");
        user.setPassword("password - 002");

        ((BookServiceImpl)bookService).update(book, user);
    }

    /**
     * 测试分布式事务
     */
    @Test
    public void delete() {
        int delete = ((BookServiceImpl) bookService).delete(11L);
        Assert.assertEquals(1, delete);
    }
}