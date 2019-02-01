package com.example.atomikos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 书
 *
 * @author fengxuechao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDO implements Serializable {

    private static final long serialVersionUID = 3231762613546697469L;

    private Long id;

    private String name;

    private Long articleId;

    private Long userId;

}
