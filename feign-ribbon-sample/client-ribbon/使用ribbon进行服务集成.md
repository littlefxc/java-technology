# client-ribbon:使用 ribbon 进行服务集成

Spring Cloud Ribbon 是基于 Netflix Ribbon 实现的一套客户端负载均衡工具，
其主要功能是提供客户端的软件负载均衡算法，将 Netflix 的中间层服务连接在一起。

## 添加 maven 依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-hystrix</artifactId>
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

## 定义 ribbon 接口和 hystrix 实现

### 配置文件

```yaml
spring.application.name=user-ribbon
server.port=8888
eureka.client.serviceUrl.defaultZone=http://jackie:123456@alpha-eureka-01:8761/eureka/,http://jackie:123456@alpha-eureka-02:8762/eureka/,http://jackie:123456@alpha-eureka-03:8763/eureka/
```

### 核心代码

正如上文介绍的，Ribbon 是客户端负载均衡工具，所以在 restTemplate() 方法上添加 @LoadBalanced 注解实现负载均衡。

```java
package com.littlefxc.examples.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author fengxuechao
 */
@SpringCloudApplication
@RestController
public class UserRibbonApplication {

    @Autowired
    RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(UserRibbonApplication.class, args);
    }

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping("/hi")
    public String hi(@RequestParam(value = "name", defaultValue = "Artaban") String name) {
        String greeting = greeting();
        return String.format("%s, %s!", greeting, name);
    }

    /**
     * ribbon 发出的请求
     * @return
     */
    @HystrixCommand(fallbackMethod = "ribbonFallback")
    public String greeting() {
        return this.restTemplate.getForObject("http://say-hello/greeting", String.class);
    }

    /**
     * ribbon 断路器
     * @return
     */
    public String ribbonFallback() {
        return "ribbon error";
    }

}
```