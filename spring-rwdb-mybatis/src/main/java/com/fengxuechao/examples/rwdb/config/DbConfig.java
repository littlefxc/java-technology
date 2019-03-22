package com.fengxuechao.examples.rwdb.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.fengxuechao.examples.rwdb.interceptor.MybatisInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fengxuechao
 */
@Slf4j
@Configuration
@EnableTransactionManagement
public class DbConfig {

    /**
     * todo 改造使用 HikariDataSource
     *
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    public DataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 一定要加 @Primary 使得 {@link MybatisInterceptor} 插件生效
     *
     * @return
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        log.info("create routing datasource...");
        Map<Object, Object> map = new HashMap<>();
        map.put(RoutingType.MASTER, masterDataSource());
        map.put(RoutingType.SLAVE, slaveDataSource());
        RoutingDataSource routing = new RoutingDataSource();
        routing.setTargetDataSources(map);
        routing.setDefaultTargetDataSource(masterDataSource());
        return routing;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sfb = new SqlSessionFactoryBean();
        sfb.setDataSource(dataSource);
        // TODO 可以加判断是否启动多数据源配置，目的是方便多环境下在本地环境调试，不影响其他环境

        sfb.setPlugins(new Interceptor[]{new MybatisInterceptor()});
        return sfb;
    }

}
