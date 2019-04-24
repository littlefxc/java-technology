# Spring + MyBatis + MySQL主从分离

[TOC]

## 基于 Docker 的 MySQL 主从复制搭建

[基于 Docker 的 MySQL 主从复制搭建](docker-compose-mysql-master-slave/README.md)

Spring主从数据库的配置和动态数据源切换

https://www.liaoxuefeng.com/article/00151054582348974482c20f7d8431ead5bc32b30354705000

Spring动态数据源+Mybatis拦截器实现数据库读写分离

https://blog.csdn.net/FJeKin/article/details/79583744

在 `AbstractRoutingDataSource#determineTargetDataSource()` 打一个断点

![AbstractRoutingDataSource断点图.png](images/AbstractRoutingDataSource断点图.png)

## 前言

在大型应用程序中，配置主从数据库并使用读写分离是常见的设计模式。而要对现有的代码在不多改变源码的情况下

## 配置多数据源

```yaml
spring.datasource.druid.filter.slf4j.enabled=true
spring.datasource.druid.filter.slf4j.statement-create-after-log-enabled=false
spring.datasource.druid.filter.slf4j.statement-close-after-log-enabled=false
spring.datasource.druid.filter.slf4j.result-set-open-after-log-enabled=false
spring.datasource.druid.filter.slf4j.result-set-close-after-log-enabled=false

# master datasource
spring.datasource.druid.master.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.druid.master.url=jdbc:mysql://localhost:3306/learn?useSSl=false
spring.datasource.druid.master.password=root
spring.datasource.druid.master.username=root
# slave datasource
spring.datasource.druid.slave.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.druid.slave.url=jdbc:mysql://localhost:3307/learn?useSSl=false
spring.datasource.druid.slave.password=root
spring.datasource.druid.slave.username=root

spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8
spring.jackson.default-property-inclusion=always
spring.jackson.property-naming-strategy=SNAKE_CASE

mybatis.type-aliases-package=com.fengxuechao.examples.rwdb.entity
mybatis.configuration.use-generated-keys=true
mybatis.configuration.map-underscore-to-camel-case=true
mybatis.configuration.default-fetch-size=100
mybatis.configuration.default-statement-timeout=30
mybatis.configuration.cache-enabled=true

logging.level.root=info
logging.level.com.fengxuechao.examples.rwdb=debug
logging.level.druid.sql.Statement=error
```