package com.littlefxc.examples;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author fengxuechao
 * @date 2019/1/10
 */
public class JedisClusterClient {

    private static final JedisClusterClient redisClusterClient = new JedisClusterClient();
    private static int count = 0;

    private JedisClusterClient() {
    }

    public static JedisClusterClient getInstance() {
        return redisClusterClient;
    }

    public static void main(String[] args) {
        JedisClusterClient jedisClusterClient = JedisClusterClient.getInstance();
        jedisClusterClient.SaveRedisCluster();
    }

    private JedisPoolConfig getPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1000);
        config.setMaxIdle(100);
        config.setTestOnBorrow(true);
        return config;
    }

    public void SaveRedisCluster() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.213.13", 7001));
        jedisClusterNodes.add(new HostAndPort("192.168.213.14", 7003));
        jedisClusterNodes.add(new HostAndPort("192.168.213.21", 7006));
        JedisCluster jc = new JedisCluster(jedisClusterNodes, getPoolConfig());
        jc.set("cluster", "this is a redis cluster");
        String result = jc.get("cluster");
        System.out.println(result);
    }
}