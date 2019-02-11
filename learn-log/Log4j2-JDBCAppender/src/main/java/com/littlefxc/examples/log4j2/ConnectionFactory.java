package com.littlefxc.examples.log4j2;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Log4j2 ConnectionFactory
 *
 * @author fengxuechao
 */
public class ConnectionFactory {

    private final DataSource dataSource;

    private ConnectionFactory() {
        Properties properties = new Properties();
        String lineSeparator = File.separator;
        String fileName = String.join(lineSeparator,
                System.getProperty("user.dir"), "Log4j2-JDBCAppender", "src", "main", "resources", "db.properties");
        try (InputStream stream = new FileInputStream(fileName)) {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.dataSource = new DruidDataSource();
        ((DruidDataSource) this.dataSource).configFromPropety(properties);
    }

    public static Connection getDatabaseConnection() throws SQLException {
        return Singleton.INSTANCE.dataSource.getConnection();
    }

    private static interface Singleton {
        final ConnectionFactory INSTANCE = new ConnectionFactory();
    }


}