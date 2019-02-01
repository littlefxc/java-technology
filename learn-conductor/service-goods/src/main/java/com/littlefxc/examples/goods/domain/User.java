package com.littlefxc.examples.goods.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fengxuechao
 * @date 2019/1/2
 **/
@Data
@Table(name = "user")
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = -2147606242980815007L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String name;

    private String money;

}
