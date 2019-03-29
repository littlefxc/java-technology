---
title: Spring-Security第二篇
date: 2019-03-29
tags: 
- Java 
- Spring
- SpringSecurity
---

# Spring-Security第二篇

在 [Spring-Security-Oauth2第一篇](Spring-Security-Oauth2第一篇.md) 中我使用的是基于内存的用户存储。

```java
/**
 * @author fengxuechao
 * @date 2019/3/26
 */
@Order(2)
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 具体的用户权限控制实现类
     * 通过重载，自定义 user-detail 服务。
     *
     * @return
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user_1").password("123456").authorities("USER").build());
        manager.createUser(User.withUsername("user_2").password("123456").authorities("USER").build());
        return manager;
    }
}
```

`User`,`UserDetailsManagerConfigurer.UserDetailsBuilder` 类的一些常方法描述：

- accountExpired(boolean): 定义账户是否已经过期
- accountLocked(boolean): 定义账户是否已经锁定
- and(): 用来链接配置
- authorities(GrantedAuthority...): 授予某个用户一项或多项权限
- authorities(List<? extends GrantedAuthority>): 授予某个用户一项或多项权限
- authorities(String...): 授予某个用户一项或多项权限
- credentialsExpired(boolean): 定义凭证是否已经过期
- disabled(boolean): 定义账号是否已被禁用

而在实际生产环境中肯定不能这么做，同样的 Spring Security 非常灵活，内置许多常见的用户存储场景。如：关系型数据库，LDAP。

## 1. 基于数据库的用户存储

将基于内存的用户存储改为基于数据库的用户存储，非常简单：

```java
/**
 * @author fengxuechao
 * @date 2019/3/26
 */
@Order(2)
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
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
}
```

只需将 `InMemoryUserDetailsManager` 替换为 `JdbcUserDetailsManager` 的同时配置一个 `dataSource`，就足够了。

但是，因为默认的表结构关系不符合我们的需求，所以需要自定义。




