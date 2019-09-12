package com.fengxuechao.amqp.rabbitmq.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/9/12
 */
@Data
@AllArgsConstructor
public class User implements Serializable {

    private String username;

    private String password;

}
