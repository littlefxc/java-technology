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
@Entity
@Table(name = "warehouse")
public class Warehouse implements Serializable {

    private static final long serialVersionUID = -4442503954698528956L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private Long goodsId;

    private Integer count;
}
