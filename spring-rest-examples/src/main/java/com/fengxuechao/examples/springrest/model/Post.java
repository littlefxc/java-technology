package com.fengxuechao.examples.springrest.model;

import lombok.Data;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/4/18
 * @title Post
 * @project_name spring-rest-examples
 * @description TODO
 */
@Data
public class Post {

    private Long id;

    private String title;

    private String url;
}
