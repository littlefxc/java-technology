package com.fengxuechao.examples;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/9/29
 */
@RestController
public class HelloController {

    @RequestMapping("/exception")
    public String helloException() {
        throw new CustomException(400, "400,统一异常处理");
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello, world!";
    }
}
