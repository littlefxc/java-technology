# 分布式事务原理探究

## 概念梳理

首先，我在学习分布式事务的过程中，遇到了许多概念，例如：X/Open XA、两段式提交（2PC）、JTA等。
概念有点多，需要记录梳理以下。

### X/Open XA

> (摘自[维基百科](https://zh.wikipedia.org/wiki/X/Open_XA))在计算技术上，XA规范是开放群组关于分布式事务处理 (DTP)的规范。
规范描述了全局的事务管理器与局部的资源管理器之间的接口。
XA规范的目的是允许多个资源（如数据库，应用服务器，消息队列，等等）在同一事务中访问，
这样可以使ACID属性跨越应用程序而保持有效。
XA使用两阶段提交来保证所有资源同时提交或回滚任何特定的事务。
XA规范描述了资源管理器要支持事务性访问所必需做的事情。
遵守该规范的资源管理器被称为XA compliant。

### DTP

DTP(全称Distributed Transaction Processing Reference Model)
