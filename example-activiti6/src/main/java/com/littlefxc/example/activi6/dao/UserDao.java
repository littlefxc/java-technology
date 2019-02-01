package com.littlefxc.example.activi6.dao;

import com.littlefxc.example.activi6.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper//加上该注解才能使用@MapperScan扫描到
public interface UserDao {

    List<User> getUserByIds(@Param("ids") List<Integer> ids);
}