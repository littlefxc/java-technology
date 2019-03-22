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