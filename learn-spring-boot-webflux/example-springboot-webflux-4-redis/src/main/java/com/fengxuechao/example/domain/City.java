package com.fengxuechao.example.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author fengxuechao
 * @date 12/14/2018
 */
@Data
public class City implements Serializable {

    private static final long serialVersionUID = -2083926831350867435L;

    /**
     * 城市编号
     */
    @Id
    private Long id;

    /**
     * 省份编号
     */
    private Long provinceId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 描述
     */
    private String description;
}
