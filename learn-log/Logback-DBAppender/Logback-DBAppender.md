# Logback-DBAppender

DBAppender以独立于Java编程语言的格式将日志事件插入到三个数据库表中。
这三个表是 `logging_event`、`logging_event_property` 和 `logging_event_exception`。
在使用DBAppender之前，它们必须存在。Logback附带了创建表的SQL脚本。
它们可以在 `logback-classic/src/main/java/ch/qos/logback/classic/db/script` 文件夹下找到。
对于每个最流行的数据库系统，都有一个特定的脚本。如果您的特定类型的数据库系统缺少脚本，那么应该很容易以现有脚本为例编写一个脚本。
如果JDBC驱动程序支持JDBC 3.0规范中引入的getGeneratedKeys方法，假设您已经创建了如上所述的适当的数据库表，那么就不需要额外的步骤。
否则，必须有适合您的数据库系统的sql方言。目前，logback有H2、HSQL、MS SQL Server、MySQL、Oracle、PostgreSQL、SQLLite和Sybase等多种方言。

如前所述，logback使用三个表来存储日志事件数据:

```sql
BEGIN;
DROP TABLE IF EXISTS logging_event_property;
DROP TABLE IF EXISTS logging_event_exception;
DROP TABLE IF EXISTS logging_event;
COMMIT;


BEGIN;
CREATE TABLE logging_event
(
    timestmp         BIGINT NOT NULL,
    formatted_message  TEXT NOT NULL,
    logger_name       VARCHAR(254) NOT NULL,
    level_string      VARCHAR(254) NOT NULL,
    thread_name       VARCHAR(254),
    reference_flag    SMALLINT,
    arg0              VARCHAR(254),
    arg1              VARCHAR(254),
    arg2              VARCHAR(254),
    arg3              VARCHAR(254),
    caller_filename   VARCHAR(254) NOT NULL,
    caller_class      VARCHAR(254) NOT NULL,
    caller_method     VARCHAR(254) NOT NULL,
    caller_line       CHAR(4) NOT NULL,
    event_id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);
COMMIT;

BEGIN;
CREATE TABLE logging_event_property
(
    event_id	      BIGINT NOT NULL,
    mapped_key        VARCHAR(254) NOT NULL,
    mapped_value      TEXT,
    PRIMARY KEY(event_id, mapped_key),
    FOREIGN KEY (event_id) REFERENCES logging_event(event_id)
);
COMMIT;

BEGIN;
CREATE TABLE logging_event_exception
(
    event_id         BIGINT NOT NULL,
    i                SMALLINT NOT NULL,
    trace_line       VARCHAR(254) NOT NULL,
    PRIMARY KEY(event_id, i),
    FOREIGN KEY (event_id) REFERENCES logging_event(event_id)
);
COMMIT;
```

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
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
        <optional>true</optional>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <optional>true</optional>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.10</version>
    </dependency>
</dependencies>
```

## 日志配置文件：logback.xml

`ch.qos.logback.classic.db.DBAppender` 是 logback 中 DBAppender 的实现类。

`ch.qos.logback.core.db.DataSourceConnectionSource` 是 DBAppender 的管理数据源的类。

`com.alibaba.druid.pool.DruidDataSource` 是数据源连接池，你也可以选择其它数据源连接池。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="DB" class="ch.qos.logback.classic.db.DBAppender">
        <connectionSource class="ch.qos.logback.core.db.DataSourceConnectionSource">
            <dataSource class="com.alibaba.druid.pool.DruidDataSource">
                <!-- DruidDataSource 中的属性配置 -->
                <driverClassName>com.mysql.jdbc.Driver</driverClassName>
                <url>jdbc:mysql://192.168.120.63:3306/learn?useSSL=false</url>
                <username>root</username>
                <password>123456</password>
            </dataSource>
        </connectionSource>
    </appender>

    <logger name="com.littlefxc.examples.logback" level="debug"/>

    <root level="debug">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="DB" />
    </root>
</configuration>

```

## 启动

```java
package com.littlefxc.examples.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fengxuechao
 * @date 2019/2/12
 **/
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        log.debug("This is debug");
        long r = System.currentTimeMillis();
        System.out.println(r - l);
        log.info("This is info");
        log.warn("This is warn");
        log.error("This is error");
        log.error("This is exception", new RuntimeException("this is a exception"));
    }
}
```