package com.fengxuechao.examples.sso.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 请求页面分发，注意和WebMvcConfig的对比，功能类似 
 * @author Veiking 
 */  
@Controller
public class PageController {  
      
    @RequestMapping("/admin")
    public String admin(Model model, String tt) {
        return "admin";  
    }  
      
    @RequestMapping("/hello")  
    public String hello(Model model, String tt) {  
        return "hello";  
    }  
}  