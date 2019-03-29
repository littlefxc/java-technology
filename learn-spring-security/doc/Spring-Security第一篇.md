---
title: Spring-Security第一篇
date: 2019-03-29
tags: 
- Java 
- Spring
- SpringSecurity
---

# Spring Security 第一篇

在 spring security 中，我们要指定 web 安全相关的细节，需要我们通过重载 `WebSecurityConfigurerAdapter` 中的一个或多个方法来实现。

通过重载 WebSecurityConfigureAdapter 的三个 configure() 方法，来配置 web 安全性的细节。这个过程会使用传递进来的参数设置行为。

- configure(WebSecurity) 通过重载，配置Spring Security 的 Filter 链。
- configure(HttpSecurity) 通过重载，配置如何通过拦截器保护请求。
- configure(AuthenticationManagerBuiler) 通过重在配置 user-detail 服务。

源码：

```java
public abstract class WebSecurityConfigurerAdapter implements
		WebSecurityConfigurer<WebSecurity> {
    
    
    // 配置如何通过拦截器保护请求
    protected void configure(HttpSecurity http) throws Exception {
    	logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");
    
    	http
    		.authorizeRequests()
    			.anyRequest().authenticated()
    			.and()
    		.formLogin().and()
    		.httpBasic();
    }
    
    // 省略其他方法
}
```

这个简单的默认配置指定了该如何保护HTTP请求，以及客户端认证用户的方案。
通过调用 authorizeRequests() 和 anyRequest().authenticated() 就会要求所有进入应用的 HTTP请求都要进行认证。
它也配置 Spring Security 支持基于表单的登录以及 HTTP Basic 方式的认证。