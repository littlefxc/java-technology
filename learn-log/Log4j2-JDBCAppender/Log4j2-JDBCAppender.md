# Log4j2-JDBCAppender

JDBCAppender使用标准JDBC将日志事件写入关系数据库表。可以将其配置为使用JNDI数据源或自定义工厂方法获取JDBC连接。
无论采用哪种方法，都必须有连接池作为支持。否则，日志记录性能将受到很大影响。

## maven 依赖

通过使用 `platform-bom` 来管理依赖的版本问题。使用 druid 作为 JDBCAppender 的连接池。

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.spring.platform</groupId>
            <artifactId>platform-bom</artifactId>
            <version>Cairo-RELEASE</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-slf4j-impl</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.10</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
</dependencies>
```

## 日志配置文件：log4j2.xml

`<JDBC></JDBC>` 是 Log4j2 的 JDBCAppender 的标签。

`ConnectionFactory` 是一个实现 Druid 连接池的单例类， 通过 `method` 属性来获取 `java.sql.Connection`。

`Column` 表示数据表 `log4j2` 的字段。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<Configuration status="INFO">
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"/>
        </Console>
        <!-- JDBCAppender -->
        <JDBC name="jdbc" tableName="log4j2">
            <ConnectionFactory class="com.littlefxc.examples.log4j2.ConnectionFactory" method="getDatabaseConnection"/>
            <!-- 数据表 log4j2 中的字段 -->
            <Column name="time" pattern="%d{yyyy-MM-dd HH:mm:ss.SSS}" />
            <Column name="level" pattern="%level"/>
            <Column name="logger" pattern="%logger"/>
            <Column name="message" pattern="%message"/>
            <Column name="exception" pattern="%ex{full}"/>
        </JDBC>
    </Appenders>
    <Loggers>
        <Logger name="com.littlefxc.examples.log4j2" level="debug" additivity="false">
            <AppenderRef ref="jdbc"/>
        </Logger>
        <Root level="debug" additivity="false">
            <AppenderRef ref="console"/>
        </Root>
    </Loggers>
</Configuration>
```

## 数据库文件：schema.sql

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS log4j2;
CREATE TABLE `log4j2`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `time` char(23) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `level` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `logger` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `exception` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
```

## ConnectionFactory

```java
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
```

`ConnectionFactory` 是一个实现 Druid 连接池的单例类。

### db.properties

```yaml
druid.url=jdbc:mysql://192.168.120.63:3306/learn?useSSL=false
druid.username=root
druid.password=123456
druid.driverClassName=com.mysql.jdbc.Driver
druid.maxActive=10
druid.minIdle=5
```

## 启动

```java
package com.littlefxc.examples.log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fengxuechao
 * @date 2019/2/11
 **/
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.debug("This is debug");
        log.info("This is info");
        log.warn("This is warn");
        log.error("This is error");
        log.error("This is error", new RuntimeException("this is a exception"));
    }
}
```