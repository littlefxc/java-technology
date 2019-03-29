package com.fengxuechao.examples.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 请求页面分发，注意和WebMvcConfig的对比，功能类似
 *
 * @author fengxuechao
 * @date 2019-03-29
 */
@Slf4j
@Controller
public class PageController {

    @RequestMapping("/admin")
    @PreAuthorize("hasAuthority('R_ADMIN')")
    public String admin(Model model, String tt) {
        return "admin";
    }

    @RequestMapping("/hello")
    public String hello(Model model, String tt) {
        return "hello";
    }
}  