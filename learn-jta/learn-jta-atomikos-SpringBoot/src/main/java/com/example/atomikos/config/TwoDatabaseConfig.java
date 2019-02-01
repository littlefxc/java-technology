package com.example.atomikos.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * 设置JTA(Atomikos)数据源，Mybatis
 *
 * @author fengxuechao
 */
@Configuration
@MapperScan(basePackages = "com.example.atomikos.dao.two", sqlSessionFactoryRef = "twoSqlSessionFactory")
public class TwoDatabaseConfig {

    /**
     * 设置JTA(Atomikos)数据源
     *
     * @return {@link AtomikosDataSourceBean}
     */
    @Bean(name = "twoAtomikosDataSource")
    @ConfigurationProperties(prefix = "spring.jta.atomikos.datasource.two")
    public DataSource oneDataSource(Environment env) {
        AtomikosDataSourceBean bean = new AtomikosDataSourceBean();
        bean.setPoolSize(env.getProperty("spring.jta.atomikos.datasource.two.pool-size", Integer.class, 10));
        return bean;
    }

    /**
     * 设置Mybatis的会话工厂类
     *
     * @param dataSource JTA(Atomikos)数据源
     * @return {@link SqlSessionFactoryBean#getObject()}
     * @throws Exception
     */
    @Bean(name = "twoSqlSessionFactory")
    public SqlSessionFactory oneSqlSessionFactory(@Qualifier("twoAtomikosDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }
}