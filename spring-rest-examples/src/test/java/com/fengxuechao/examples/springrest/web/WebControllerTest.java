package com.fengxuechao.examples.springrest.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 不启动服务器的情况下创建整个上下文，使用@SpringBootTest注释
 * 添加@AutoConfigureMockMvc  来注入一个  MockMvc 实例并发送HTTP请求
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/4/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void foos() throws Exception {
        this.mockMvc.perform(get("/foos"))
                .andExpect(status().isOk())
                .andExpect(content().string("foos"));
    }
}