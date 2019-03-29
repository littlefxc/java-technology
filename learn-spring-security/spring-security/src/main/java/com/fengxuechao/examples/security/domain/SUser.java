package com.fengxuechao.examples.security.domain;

import lombok.Data;

/**
 * 用户名密码信息
 * @author fengxuechao
 * @date 2019-03-29
 */
@Data
public class SUser {
    private int id;
    private String name;
    private String password;

    public SUser() {
    }

    public SUser(SUser sUser) {
        this.id = sUser.getId();
        this.name = sUser.getName();
        this.password = sUser.getPassword();
    }
} 