package com.example.atomikos.service.impl;

import com.example.atomikos.entity.BookDO;
import com.example.atomikos.entity.UserDO;
import com.example.atomikos.service.BookService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml", "classpath:spring-tx.xml"})
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
        UserDO user = new UserDO();
        user.setId(16L);
        user.setUsername("username - 002");
        user.setPassword("password - 002");

        BookDO book = new BookDO();
        book.setId(13L);
        book.setName("Book Name - 002");
        book.setArticleId(69L);
        book.setUserId(user.getId());



        ((BookServiceImpl) bookService).update(book, user);
    }

    /**
     * 没有事务管理
     */
    @Test
    public void delete() {
        int delete = ((BookServiceImpl) bookService).delete(12L);
        Assert.assertEquals(0, delete);
    }
}