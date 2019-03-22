# Spring + MyBatis + MySQL主从分离

[TOC]

## Navicat 破解

https://github.com/DoubleLabyrinth/navicat-keygen/blob/mac/README.zh-CN.md

https://linan.blog/2018/Navicat-Premium/

## 基于 Docker 的 MySQL 主从复制搭建

https://juejin.im/post/5b3a24dde51d4555b3360253

### 测试

```shell
docker-compose up -d
docker-compose logs -f mysqlconfigure
docker-compose exec mysqlmaster mysql -uroot -proot -e "CREATE DATABASE test_replication;"
docker-compose exec mysqlslave mysql -uroot -proot -e "SHOW DATABASES;"
```

### Spring主从数据库的配置和动态数据源切换

https://www.liaoxuefeng.com/article/00151054582348974482c20f7d8431ead5bc32b30354705000

### Spring动态数据源+Mybatis拦截器实现数据库读写分离

https://blog.csdn.net/FJeKin/article/details/79583744

#### 测试

在 `AbstractRoutingDataSource#determineTargetDataSource()` 打一个断点

![AbstractRoutingDataSource断点图.png](images/AbstractRoutingDataSource断点图.png)