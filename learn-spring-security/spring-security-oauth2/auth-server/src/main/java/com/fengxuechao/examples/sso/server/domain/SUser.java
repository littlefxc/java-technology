package com.fengxuechao.examples.sso.server.domain;

import lombok.Data;

/**
 * 用户名密码信息
 *
 * @author Veiking
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