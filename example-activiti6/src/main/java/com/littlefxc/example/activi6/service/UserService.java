package com.littlefxc.example.activi6.service;

import com.littlefxc.example.activi6.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserService extends JpaRepository<User, Long> {
    public List<User> findByName(String name);
}