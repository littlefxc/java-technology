package com.fengxuechao.examples;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private static final long serialVersionUID = 3755973060649390798L;

    private String content;

    private Date date;

}
