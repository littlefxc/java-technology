# Spring + MyBatis + MySQL主从分离

[TOC]

## 基于 Docker 的 MySQL 主从复制搭建

###  配置 MySQL

- windows 平台：

    ```docker
    docker run --name mysql_master -p 3306:3306 -v E:\conf\mysql:/etc/mysql/conf.d -v E:\logs:/logs -v E:\data\mysql\master:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql
    ```
    
    使用同样的同样的方式创建 Slave 服务器，注意修改 `--name`
    
    ```docker
    docker run --name mysql_slave_1 -p 3306:3307 -v ~/conf/mysql:/etc/mysql/conf.d -v E:\logs:/logs -v E:\data\mysql\master:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql
    ```
    
- linux/unix 平台：

    ```docker
    docker run --name mysql_master -p 3306:3306 -v E:\conf\mysql:/etc/mysql/conf.d -v ~/logs:/logs -v ~/data/mysql/master:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql
    ```
    
    使用同样的同样的方式创建 Slave 服务器，注意修改 `--name`
    
    ```docker
    docker run --name mysql_slave_1 -p 3306:3307 -v ~/conf/mysql:/etc/mysql/conf.d -v ~/logs:/logs -v ~/data/mysql/master:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql
    ```

### 配置主服务器（Master）

### 配置从服务器（Slave）

### 完成Master和Slave链接

### 测试配置是否成功