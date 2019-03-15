package com.fengxuechao.examples.security.service.impl;

import com.fengxuechao.examples.security.constant.RoleConstant;
import com.fengxuechao.examples.security.entity.UserEntity;
import com.fengxuechao.examples.security.mapper.UserMapper;
import com.fengxuechao.examples.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Primary
@Slf4j
public class BaseUserService implements UserService {

    private final UserMapper userMapper;

    public BaseUserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public boolean insert(UserEntity userEntity) {
        String username = userEntity.getUsername();
        if (exist(username))
            return false;
        userEntity.setRoles(RoleConstant.ROLE_USER);
        encryptPassword(userEntity);// 用户注册时密码加密
        int result = userMapper.insert(userEntity);
        return result == 1;
    }

    /**
     * 加密密码
     */
    private void encryptPassword(UserEntity userEntity) {
        String password = userEntity.getPassword();
        password = new BCryptPasswordEncoder().encode(password);
        userEntity.setPassword(password);
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 判断用户是否存在
     *
     * @param username 账号
     * @return 密码
     */
    private boolean exist(String username) {
        UserEntity userEntity = userMapper.selectByUsername(username);
        return (userEntity != null);
    }

}
