package com.fengxuechao.examples.auth.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Redis 配置
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/6/24
 */
@EnableConfigurationProperties(RedisProperties.class)
@Configuration
public class RedisConfig {

    /**
     * 使用 lettuce 作为 redis 的连接池
     *
     * @param configuration
     * @return
     */
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisClusterConfiguration configuration) {
        return new LettuceConnectionFactory(configuration);
    }

    /**
     * lettuce 集群配置
     */
    @Bean
    public RedisClusterConfiguration getClusterConfiguration(RedisProperties redisProperties) {
        RedisProperties.Cluster clusterProperties = redisProperties.getCluster();
        RedisClusterConfiguration config = new RedisClusterConfiguration(clusterProperties.getNodes());

        if (clusterProperties.getMaxRedirects() != null) {
            config.setMaxRedirects(clusterProperties.getMaxRedirects());
        }
        return config;
    }
}
