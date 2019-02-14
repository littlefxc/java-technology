# MySQL 中基于 XA 实现的分布式事务

## 

## 问题记录

MySQL 8.x版本更新安全插件 `mysql_native_password` 变为 `caching_sha2_password`

Unable to load authentication plugin 'caching_sha2_password'.

解决办法：升级 `mysql-connector-java` 依赖的版本。