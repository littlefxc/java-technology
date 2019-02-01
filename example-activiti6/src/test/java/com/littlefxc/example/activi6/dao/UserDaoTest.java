package com.littlefxc.example.activi6.dao;

import com.littlefxc.example.activi6.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author fengxuechao
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Test
    public void getUserByIds() {
        userDao.getUserByIds(Arrays.asList(1, 2)).forEach(System.out::println);
    }
}