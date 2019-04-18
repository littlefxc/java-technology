package com.fengxuechao.examples.springrest.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 避免创建整个上下文并仅测试MVC控制器，使用 @WebMvcTest
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/4/18
 */
@RunWith(SpringRunner.class)
@WebMvcTest(WebController.class)
public class WebControllerTestWithoutContext {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void foos() throws Exception {
        this.mockMvc.perform(get("/foos"))
                .andExpect(status().isOk())
                .andExpect(content().string("foos"));
    }
}