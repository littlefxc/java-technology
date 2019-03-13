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
