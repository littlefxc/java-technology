# 在SpringBoot1.5.x下如何使RedisTokenStore集群化

在 spring boot 1.5.x 下 `spring-boot-starter-data-redis` 默认使用 jedis 作为客户端。

因为 `JedisCluster` 不支持集群的