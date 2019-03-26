---
title: Spring-Security-Oauth2第一篇
date: 2019-03-26 15:27:00
tags: 
- Java 
- Spring
- SpringSecurityOauth2
---

# Spring-Security-Oauth2第一篇

## 默认数据结构

[spring-oauth-server 数据库表](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql)

## ResourceServerConfiguration 和 SecurityConfiguration上配置的顺序

SecurityConfiguration一定要在ResourceServerConfiguration 之前，因为spring实现安全是通过添加过滤器(Filter)来实现的，
基本的安全过滤应该在oauth过滤之前, 所以在SecurityConfiguration设置@Order(2), 在ResourceServerConfiguration上设置@Order(6)
