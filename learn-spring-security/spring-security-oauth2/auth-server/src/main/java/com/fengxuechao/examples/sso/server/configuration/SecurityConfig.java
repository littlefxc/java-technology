package com.fengxuechao.examples.sso.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

/**
 * Security 主配置文件
 *
 * @author fengxuechao
 * @date 2019/3/26
 */
@Order(2)
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    /**
     * 具体的用户权限控制实现类
     * 通过重载，自定义 user-detail 服务。
     *
     * @return
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
        manager.setDataSource(dataSource);
        manager.createUser(User.withUsername("user_1").password("123456").authorities("USER").build());
        manager.createUser(User.withUsername("user_2").password("123456").authorities("USER").build());
        return manager;
    }

    /**
     * 安全策略配置
     * 通过重载，配置如何通过拦截器保护请求
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http
                .authorizeRequests()
                // 对于网站部分资源需要指定鉴权
                //.antMatchers("/admin/**").hasRole("ADMIN")
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated().and()
                // 定义当需要用户登录时候，转到的登录页面
                .formLogin().loginPage("/login").defaultSuccessUrl("/index").permitAll().and()
                // 定义登出操作
                .logout().logoutSuccessUrl("/login?logout").permitAll()
                .and().csrf().disable()
        ;
        // 禁用缓存
        http.headers().cacheControl();*/

        http
                .csrf().disable()
                .requestMatchers().antMatchers("/oauth/**", "/", "/login")
                .and().authorizeRequests().anyRequest().authenticated()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/index").permitAll()
                .and().logout().logoutSuccessUrl("/login?logout").permitAll();
    }

    /**
     * 定义不需要过滤的静态资源（等价于HttpSecurity的permitAll）
     * 通过重载，配置Spring Security的Filter链
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
    }

    /**
     * 通过重载，配置user-detail 服务。
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }
}
