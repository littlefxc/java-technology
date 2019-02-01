package com.example.atomikos.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * 设置JTA(Atomikos)数据源，Mybatis
 *
 * @author fengxuechao
 */
@Configuration
@MapperScan(basePackages = "com.example.atomikos.dao.one", sqlSessionFactoryRef = "oneSqlSessionFactory")
public class OneDatabaseConfig {

    /**
     * 设置JTA(Atomikos)数据源
     *
     * @return {@link AtomikosDataSourceBean}
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.jta.atomikos.datasource.one")
    public DataSource oneDataSource(Environment env) {
        AtomikosDataSourceBean bean = new AtomikosDataSourceBean();
        bean.setPoolSize(env.getProperty("spring.jta.atomikos.datasource.one.pool-size", Integer.class, 10));
        return bean;
    }

    /**
     * 设置Mybatis的会话工厂类
     *
     * @param dataSource JTA(Atomikos)数据源
     * @return {@link SqlSessionFactoryBean#getObject()}
     * @throws Exception
     */
    @Primary
    @Bean(name = "oneSqlSessionFactory")
    public SqlSessionFactory oneSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }
}