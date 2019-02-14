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
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.*;

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
     * 测试分布式事务(无分布式事务管理)
     */
    @Test
    public void delete() {
        int delete = ((BookServiceImpl) bookService).delete(13L);
        Assert.assertEquals(1, delete);
    }

    @Autowired
    JtaTransactionManager jtaTransactionManager;

    /**
     * 测试分布式事务(编程式)
     */
    @Test
    public void testUserTransaction() {
        UserTransaction userTransaction = jtaTransactionManager.getUserTransaction();
        try{
            assert userTransaction != null;
            userTransaction.begin();
            int delete = ((BookServiceImpl) bookService).delete(16L);
            try {
                userTransaction.commit();
            } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
            try {
                userTransaction.rollback();
            } catch (SystemException e1) {
                e1.printStackTrace();
            }
        }

    }
}