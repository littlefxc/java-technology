package com.fengxuechao.examples.auth.redis;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.util.ClassUtils;
import redis.clients.jedis.JedisCluster;

import java.lang.reflect.Proxy;

public class JedisConnectionWrapperClusterInvocationHandler implements MethodInterceptor, DisposableBean {

    protected RedisConnection createRedisConnectionProxy(JedisCluster jedisCluster, int redirections) {
        Class<?>[] ifcs = ClassUtils.getAllInterfacesForClass(RedisConnection.class, getClass().getClassLoader());
        return (RedisConnection) Proxy.newProxyInstance(jedisCluster.getClass().getClassLoader(), ifcs,
                new JedisConnectionWrapper(jedisCluster, redirections));
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> returnType = invocation.getMethod().getReturnType();
        Object returnVal = null;
        // 只拦截获取RedisConnection的getConnection方法
        if (returnType.isAssignableFrom(RedisConnection.class)) {
//            returnVal = createRedisConnectionProxy(jedisCluster, redirections);
        } else {
            returnVal = invocation.proceed();
        }
        return returnVal;
    }

    @Override
    public void destroy() throws Exception {

    }
}