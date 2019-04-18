package com.fengxuechao.examples.springrest.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/4/18
 * @title RestController
 * @project_name spring-rest-examples
 * @description TODO
 */
@RestController
public class WebController {


    @RequestMapping("/foos")
    public String foos() {
        return "foos";
    }
}
