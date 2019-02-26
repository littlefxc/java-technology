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

RestConfiguration

```java
package com.littlefxc.examples.cloud;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author fengxuechao
 * @date 2019/2/26
 **/
@Configuration
public class RestConfiguration {

    /**
     * Ribbon 是客户端负载均衡工具，所以在 getRestTemplate 方法上添加 @LoadBalanced 注解实现负载均衡
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * Ribbon 提供 IRule 接口，该接口定义了如何访问服务的策略,以下是该接口的实现类：
     * <ol>
     *     <li>RoundRobinRule：轮询，默认使用的规则；</li>
     *     <li>RandomRule：随机；</li>
     *     <li>AvailabilityFilteringRule：先过滤由于多次访问故障而处于断路器跳闸状态以及并发连接数量超过阀值得服务，然后从剩余服务列表中按照轮询策略进行访问；</li>
     *     <li>WeightedResponseTimeRule：根据平均响应时间计算所有的权重，响应时间越快服务权重越有可能被选中；</li>
     *     <li>RetryRule：先按照 RoundRobinRule 策略获取服务，如果获取服务失败则在指定时间内进行重试，获取可用服务；</li>
     *     <li>BestAvailableRule：先过滤由于多次访问故障而处于断路器跳闸状态的服务，然后选择并发量最小的服务；</li>
     *     <li>ZoneAvoidanceRule：判断 server 所在区域的性能和 server 的可用性来选择服务器。</li>
     * </ol>
     *
     * @return
     */
    @Bean
    public IRule iRule() {
        return new RandomRule();
    }
}
```

Ribbon 提供 IRule 接口，该接口定义了如何访问服务的策略，以下是该接口的实现类：

1) RoundRobinRule：轮询，默认使用的规则；
2) RandomRule：随机；
3) AvailabilityFilteringRule：先过滤由于多次访问故障而处于断路器跳闸状态以及并发连接数量超过阀值得服务，然后从剩余服务列表中按照轮询策略进行访问；
4) WeightedResponseTimeRule：根据平均响应时间计算所有的权重，响应时间越快服务权重越有可能被选中；
5) RetryRule：先按照 RoundRobinRule 策略获取服务，如果获取服务失败则在指定时间内进行重试，获取可用服务；
6) BestAvailableRule：先过滤由于多次访问故障而处于断路器跳闸状态的服务，然后选择并发量最小的服务；
7) ZoneAvoidanceRule：判断 server 所在区域的性能和 server 的可用性来选择服务器。

UserRibbonApplication

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