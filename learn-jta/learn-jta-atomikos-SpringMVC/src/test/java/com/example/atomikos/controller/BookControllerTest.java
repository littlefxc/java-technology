package com.example.atomikos.controller;

import com.example.atomikos.dao.one.UserDao;
import com.example.atomikos.entity.BookVo;
import com.example.atomikos.entity.UserDO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(name = "parent", locations = {"classpath:spring-context.xml", "classpath:spring-tx.xml"}),
        @ContextConfiguration(name = "child", locations = {"classpath:spring-mvc.xml"})
})
public class BookControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserDao userDao;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    /**
     * 申明式
     *
     * @throws Exception
     */
    @Test
    public void save() throws Exception {
        UserDO user = new UserDO();
        user.setUsername("username - 002");
        user.setPassword("password - 002");

        BookVo book = new BookVo();
        book.setName("Book Name - 002");
        book.setArticleId(69L);
        book.setUser(user);
        String json = objectMapper.writeValueAsString(book);
        this.mockMvc.perform(
                post("/books")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Book Name - 002")))
                .andExpect(jsonPath("$.articleId", is(69)))
                .andDo(print());
    }

    /**
     * 注解式
     *
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        UserDO user = userDao.get(3L);
        assert user != null;
        user.setUsername("username - 003");
        user.setPassword("password - 003");

        BookVo book = new BookVo();
        book.setId(3L);
        book.setName("Book Name - 003");
        book.setArticleId(69L);
        book.setUser(user);

        String json = objectMapper.writeValueAsString(book);
        this.mockMvc.perform(
                put("/books")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Book Name - 003")))
                .andExpect(jsonPath("$.articleId", is(69)))
                .andDo(print());
    }

    /**
     * 没有事务管理
     *
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/4"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}