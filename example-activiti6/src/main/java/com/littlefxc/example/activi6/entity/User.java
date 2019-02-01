package com.littlefxc.example.activi6.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户信息表
 */
@Data
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 8478538943508185014L;

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户身份标识（1-申请者，2-审核者）
     */
    private Integer type;

    private Integer delete_flag;
}