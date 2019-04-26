package com.fengxuechao.examples.rwdb.interceptor;

import com.fengxuechao.examples.rwdb.config.RoutingDataSourceContext;
import com.fengxuechao.examples.rwdb.config.RoutingType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拦截器，对update使用写库，对query使用读库
 * 优势：源代码不变（通过mybatis拦截器），易扩展
 *
 * @author fengxuechao
 * @date 2019-03-22
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
})
@Slf4j
public class MybatisInterceptor implements Interceptor {

    /**
     * 用map对象缓存数据，数据量大的话，此处需要优化。
     */
    private static final Map<String, RoutingType> cacheMap = new ConcurrentHashMap<>();

    /**
     * 通过正则表达式，拦截sql语句匹配类型设置数据源。
     */
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if (!synchronizationActive) {
            Object[] objects = invocation.getArgs();
            MappedStatement ms = (MappedStatement) objects[0];

            RoutingType routingType;

            if ((routingType = cacheMap.get(ms.getId())) == null) {
                //读方法
                if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                    //!selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
                    if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                        routingType = RoutingType.MASTER;
                    } else {
                        BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                        String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                        if (sql.matches(REGEX)) {
                            routingType = RoutingType.MASTER;
                        } else {
                            routingType = RoutingType.SLAVE;
                        }
                    }
                } else {
                    routingType = RoutingType.MASTER;
                }
                if (log.isDebugEnabled()){
                    log.debug("设置方法[{}] use [{}] Strategy, SqlCommandType [{}]..", ms.getId(), routingType.name(), ms.getSqlCommandType().name());
                }
                cacheMap.put(ms.getId(), routingType);
            }
            RoutingDataSourceContext.setRoutingType(routingType);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
