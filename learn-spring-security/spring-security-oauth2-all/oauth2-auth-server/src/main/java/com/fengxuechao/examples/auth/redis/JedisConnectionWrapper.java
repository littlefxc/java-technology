package com.fengxuechao.examples.auth.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class JedisConnectionWrapper implements InvocationHandler {
    private static final String CLOSE = "close";
    private static final String HASH_CODE = "hashCode";
    private static final String EQUALS = "equals";
    private static final String MULTI = "multi";
    private static final String EXEC = "exec";
    private JedisClusterConnectionHandler connectionHandler;
    static Method METHOD_GETCONNECTION = null;
    static Method METHOD_GETCONNECTIONFROMSLOT = null;
    static Field FIELD_CONNECTIONHANDLER = null;
    private ThreadLocal<Jedis> askConnection = new ThreadLocal<Jedis>();
    private int redirections;

    static {
        METHOD_GETCONNECTION = ReflectionUtils.findMethod(JedisClusterConnectionHandler.class, "getConnection");
        METHOD_GETCONNECTION.setAccessible(true);
        METHOD_GETCONNECTIONFROMSLOT = ReflectionUtils.findMethod(JedisClusterConnectionHandler.class, "getConnectionFromSlot", int.class);
        METHOD_GETCONNECTIONFROMSLOT.setAccessible(true);
        FIELD_CONNECTIONHANDLER = ReflectionUtils.findField(JedisCluster.class, "connectionHandler");
        FIELD_CONNECTIONHANDLER.setAccessible(true);
    }

    @SuppressWarnings("unchecked")
    public JedisConnectionWrapper(JedisCluster jedisCluster, int redirections) {
        this.redirections = redirections;
        connectionHandler = (JedisClusterConnectionHandler) ReflectionUtils.getField(FIELD_CONNECTIONHANDLER, jedisCluster);
        Assert.notNull(connectionHandler, "connectionHandler 获取失败");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals(EQUALS)) {
            // Only consider equal when proxies are identical.
            return (proxy == args[0]);
        } else if (method.getName().equals(HASH_CODE)) {
            // Use hashCode of PersistenceManager proxy.
            return System.identityHashCode(proxy);
        } else if (method.getName().equals(CLOSE)) { // Handle close
            // method: suppress, not valid. 下文中用完了就关了，所以调这个close也没有意义，索性屏蔽掉
            return null;
        } else if (method.getName().equals(MULTI)) {
            // 集群不支持事务，所以跳过
            log.warn("集群不支持事务，所以跳过:{}", MULTI);
            return null;
        } else if (method.getName().equals(EXEC)) {
            // 集群不支持事务，所以跳过
            log.warn("集群不支持事务，所以跳过:{}", EXEC);
            return null;
        }
        // Invoke method on target RedisConnection.
        Object retVal = runWithRetries(method, args, redirections, false, false);
        return retVal;
    }

    private Object runWithRetries(Method method, Object[] args, int redirections, boolean tryRandomNode, boolean asking) {
        if (redirections <= 0) {
            throw new JedisClusterMaxRedirectionsException("Too many Cluster redirections?");
        }
        Map<String, JedisPool> map = connectionHandler.getNodes();
        Assert.notEmpty(map, "没有可用的连接资源");
        Jedis connection = null;
        // if (null == connection) {
        // connection =
        // map.entrySet().iterator().next().getValue().getResource();
        // }
        JedisConnection jconnection = null;
        JedisConnection target;
        try {
            if (asking) {
                // TODO: Pipeline asking with the original command to make it
                // faster....
                connection = askConnection.get();
                connection.asking();
                // if asking success, reset asking flag
                asking = false;
            } else {
                connection = (Jedis) METHOD_GETCONNECTION.invoke(connectionHandler);
            }
            // --------------------------
            Client client = connection.getClient();
            log.trace("redis node 当前值：" + client.getHost() + "------" + client.getPort());
            // jconnection = new JedisConnection(connection, pool, 0);// redis
            // cluster只支持0号数据库，所以这里写死
            jconnection = new JedisConnection(connection);// redis
            target = jconnection;//在这里偷偷的替换掉 jedisconnection
            Object result = method.invoke(target, args);
            String commandName = method.getName();
            log.debug("exec command:{} @{}:{}", commandName, client.getHost(), client.getPort());
            return result;
        } catch (Exception e) {
            releaseConnection(connection);
            connection = null;
            try {
                Throwable t = e.getCause();
                if (e instanceof InvocationTargetException) {// 拿出反射包裹的异常
                    t = ((InvocationTargetException) e).getTargetException();
                    if (t instanceof NestedRuntimeException) {// 拿出spring包裹的异常
                        t = t.getCause();
                    }
                }
                throw t;
            } catch (JedisRedirectionException jre) {
                // release current connection before recursion or renewing
                releaseConnection(connection);
                connection = null;
                if (jre instanceof JedisAskDataException) {
                    asking = true;
                    HostAndPort node = jre.getTargetNode();
                    log.trace("redis node 期望值：" + node.getHost() + "---EEEE---" + node.getPort());
                    askConnection.set(this.connectionHandler.getConnectionFromNode(node));
                } else if (jre instanceof JedisMovedDataException) {
                    // it rebuilds cluster's slot cache
                    // recommended by Redis cluster specification
                    this.connectionHandler.renewSlotCache();
                    asking = true;
                    HostAndPort node = jre.getTargetNode();
                    log.trace("redis node 期望值：" + node.getHost() + "---BBBBB--" + node.getPort());
                    askConnection.set(this.connectionHandler.getConnectionFromNode(node));
                } else {
                    throw new JedisClusterException(jre);
                }
                return runWithRetries(method, args, redirections - 1, false, asking);
            } catch (Throwable t) {
                e.printStackTrace();
            }
        } finally {
            releaseConnection(connection);
        }
        return null;
    }

    private void releaseConnection(Jedis connection) {
        if (connection != null) {
            connection.close();
        }
    }
}