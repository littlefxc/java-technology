---
title: Spring-Security学习记录之校验流程
date: 2019-03-08 08:42:00
tags: 
- Java 
- Spring
- SpringSecurity
---

# Spring-Security学习记录之校验流程

## Spring Security 校验流程图

![SpringSecurity校验流程图](images/SpringSecurity校验流程图.png)

## 相关解释

### AbstractAuthenticationProcessingFilter 抽象类

```java
/**
 * 调用 #requiresAuthentication(HttpServletRequest, HttpServletResponse) 决定是否需要进行验证操作。
 * 如果需要验证，则会调用 #attemptAuthentication(HttpServletRequest, HttpServletResponse) 方法。
 * 有三种结果：
 * 1、返回一个 Authentication 对象。
 * 配置的 SessionAuthenticationStrategy` 将被调用，
 * 然后 然后调用 #successfulAuthentication(HttpServletRequest，HttpServletResponse，FilterChain，Authentication) 方法。
 * 2、验证时发生 AuthenticationException。
 * #unsuccessfulAuthentication(HttpServletRequest, HttpServletResponse, AuthenticationException) 方法将被调用。
 * 3、返回Null，表示身份验证不完整。假设子类做了一些必要的工作（如重定向）来继续处理验证，方法将立即返回。
 * 假设后一个请求将被这种方法接收，其中返回的Authentication对象不为空。
 */
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (!requiresAuthentication(request, response)) {
        chain.doFilter(request, response);

        return;
    }

    if (logger.isDebugEnabled()) {
        logger.debug("Request is to process authentication");
    }

    Authentication authResult;

    try {
        authResult = attemptAuthentication(request, response);
        if (authResult == null) {
            // return immediately as subclass has indicated that it hasn't completed
            // authentication
            return;
        }
        sessionStrategy.onAuthentication(authResult, request, response);
    }
    catch (InternalAuthenticationServiceException failed) {
        logger.error(
                "An internal error occurred while trying to authenticate the user.",
                failed);
        unsuccessfulAuthentication(request, response, failed);

        return;
    }
    catch (AuthenticationException failed) {
        // Authentication failed
        unsuccessfulAuthentication(request, response, failed);

        return;
    }

    // Authentication success
    if (continueChainBeforeSuccessfulAuthentication) {
        chain.doFilter(request, response);
    }

    successfulAuthentication(request, response, chain, authResult);
}
```

### UsernamePasswordAuthenticationFilter（AbstractAuthenticationProcessingFilter的子类）

### ProviderManager（AuthenticationManager的实现类）


