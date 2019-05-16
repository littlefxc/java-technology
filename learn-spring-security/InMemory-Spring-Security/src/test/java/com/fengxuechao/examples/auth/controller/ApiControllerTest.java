package com.fengxuechao.examples.auth.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/5/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void user() throws Exception {
        MockHttpServletRequestBuilder mock = MockMvcRequestBuilders
                .post("http://localhost:8080/oauth/token?grant_type=password&scope=read&username=user&password=123456")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Basic Y2xpZW50OjEyMzQ1Ng==");
        mvc.perform(mock).andDo(print());
    }
}