package com.example.atomikos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文章
 *
 * @author fengxuechao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDO implements Serializable {

    private static final long serialVersionUID = 3971756585655871603L;

    private Long id;

    private String title;

    private String content;

    private String url;

}
