package com.littlefxc.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fengxuechao
 * @date 2018/12/27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province implements Serializable {

    private static final long serialVersionUID = 8813865573814074417L;

    /**
     * 行省ID
     */
    private Integer id;

    /**
     * 行省名
     */
    private String provinceName;

    /**
     * 行省描述
     */
    private String description;

}
