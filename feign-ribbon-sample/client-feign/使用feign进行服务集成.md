# client-feign

在微服务的实践过程中，Spring Cloud Ribbon 和 Spring Cloud Hystrix 通常一起使用。
Spring Cloud Feign 是对这两个基础工具的更高层次封装，在 Netflix Feign 的基础上扩展了对 Spring MVC 的注解支持，提供了一种声明式的 Web 服务客户端定义方式。

## 添加 maven 依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```