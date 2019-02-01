package com.example.atomikos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户
 *
 * @author fengxuechao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDO implements Serializable {

    private static final long serialVersionUID = 469663920369239035L;

    private Long id;

    private String username;

    private String password;
}
