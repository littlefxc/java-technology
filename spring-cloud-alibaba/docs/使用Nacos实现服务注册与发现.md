---
title: 使用Nacos实现服务注册与发现
date: 2019-03-13 09:18:00
tags:
- nacos
- SpringCloud
---

# 使用Nacos实现服务注册与发现

## 前言

Nacos致力于帮助您发现、配置和管理微服务。Nacos提供了一组简单易用的特性集，
帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。Nacos帮助您更敏捷和容易地构建、交付和管理微服务平台。
Nacos是构建以“服务”为中心的现代应用架构 (例如微服务范式、云原生范式) 的服务基础设施。

## nacos 是什么

Nacos致力于帮助您发现、配置和管理微服务。Nacos提供了一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。
Nacos帮助您更敏捷和容易地构建、交付和管理微服务平台。Nacos是构建以“服务”为中心的现代应用架构 (例如微服务范式、云原生范式) 的服务基础设施。

在接下里的程序中，将使用Nacos作为微服务架构中的注册中心（替代：eurekba、consul等传统方案）以及配置中心（spring cloud config）来使用。

## 安装 nacos

下载地址：[https://github.com/alibaba/nacos/releases](https://github.com/alibaba/nacos/releases)
本文版本：0.9.0

下载完成之后，解压。根据不同平台，执行不同命令，启动单机版Nacos服务：

- Linux/Unix/Mac：sh startup.sh -m standalone
- Windows：cmd startup.cmd 或者双击 startup.cmd 运行文件。

### 进入 nacos 管理界面

启动完成之后，访问：[http://127.0.0.1:8848/nacos/](http://127.0.0.1:8848/nacos/)，可以进入Nacos的服务管理页面，具体如下:

账号：nacos 密码：naocs

![nacos管理界面](images/nacos管理界面.png)

## nacos 注册中心接入应用

完成了 nacos 的启动，现在要做的事验证 nacos 的服务发现和注册。

### 服务提供者

1. 创建一个 Spring Boot 应用，命名为： `provider-nacos`

2. 编辑 `pom.xml`, 加入必要的依赖：

    ```xml
       
    ```

### 服务消费者

### 参考资料