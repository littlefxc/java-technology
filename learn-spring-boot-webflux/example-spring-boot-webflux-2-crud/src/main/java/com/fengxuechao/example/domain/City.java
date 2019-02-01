package com.fengxuechao.example.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author fengxuechao
 * @date 12/14/2018
 */
@Data
@Entity
@Table(name = "city")
public class City implements Serializable {

    private static final long serialVersionUID = -2083926831350867435L;

    /**
     * 城市编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 省份编号
     */
    @Column(name = "province_id")
    private Long provinceId;

    /**
     * 城市名称
     */
    @Column(name = "city_name")
    private String cityName;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;
}
