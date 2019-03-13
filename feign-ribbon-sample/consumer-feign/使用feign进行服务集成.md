# client-feign:使用 feign 进行服务集成

在微服务的实践过程中，Spring Cloud Ribbon 和 Spring Cloud Hystrix 通常一起使用。
Spring Cloud Feign 是对这两个基础工具的更高层次封装，在 Netflix Feign 的基础上扩展了对 Spring MVC 的注解支持，提供了一种声明式的 Web 服务客户端定义方式。

## 添加 maven 依赖

```xml
<dependencies>
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
</dependencies>
```

## 定义 feign 接口和 hystrix 实现

### feign

其中 @FeignClient 指定服务名，Spring MVC 注解绑定具体的 REST 接口及请求参数。
注意在定义参数绑定时，@RequestParam 、@RequestHeader 等注解的 value 不能省略，Spring MVC 会根据参数名作为默认值，但 Feign 中必须通过 value 指定。

```java
package com.littlefxc.examples.cloud;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author fengxuechao
 * @date 2019/2/25
 **/
@FeignClient(value = "say-hello", fallback = HelloServiceFallback.class)
public interface HelloService {

    @GetMapping("/greeting")
    String hello();
}
```

### 服务降级

Hystrix 提供的服务降级是容错的重要方法，由于 Feign 在定义服务客户端时将 HystrixCommand 的定义进行了封装，
导致无法使用 @HystrixCommand 的 fallback 参数指定降级逻辑。
Spring Cloud Feign 提供了一种简单的定义服务降级的方式，创建 HelloServiceFallback 实现 HelloService 接口，
通过 @Component 声明为 Bean，在 HelloServiceFallback 中实现具体的降级逻辑，
最后在 @FeignClient 中通过 fallback 属性声明处理降级逻辑的 Bean。

```java
package com.littlefxc.examples.cloud;

import org.springframework.stereotype.Component;

/**
 * @author fengxuechao
 * @date 2019/2/25
 **/
@Component
public class HelloServiceFallback implements HelloService {

    @Override
    public String hello() {
        return provider;
    }

}
```

## 配置文件

注意要是 hystrix 起作用，就必须添加 `feign.hystrix.enabled=true` 改变默认值。

```yaml
cloud
server.port=8889
eureka.client.serviceUrl.defaultZone=http://jackie:123456@alpha-eureka-01:8761/eureka/,http://jackie:123456@alpha-eureka-02:8762/eureka/,http://jackie:123456@alpha-eureka-03:8763/eureka/
cloud
```

## 启动

注意添加注解 `@EnableFeignClients` 使 feign 生效。

```java
package com.littlefxc.examples.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengxuechao
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class UserFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserFeignApplication.class, args);
    }

    @Autowired
    HelloService service;

    @RequestMapping("/hi")
    public String hi(@RequestParam(value = "name", defaultValue = "Artaban") String name) {
        String greeting = service.hello();
        return String.format("%s, %s!", greeting, name);
    }
}
```