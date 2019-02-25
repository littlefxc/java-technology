# Spring Cloud 1.x 使用 ribbon 或 feign 进行服务集成的示例

## 版本选择

- Spring boot : 1.5.18.RELEASE
- Spring Cloud : 1.4.5.RELEASE

## 模块间关系

被调用服务的模块名是 `service`, 目的是供以下两个使用 feign 或 ribbon 集成的服务调用。

- 被 feign 或 ribbon 调用的服务的模块名：`service`
- 使用 feign 集成的服务的模块名：`client-feign`
- 使用 ribbon 集成的服务的模块名：`client-ribbon`

## 被调用服务

[service.md](service/service.md)

## 使用 feign 进行服务集成

[使用 feign 进行服务集成](client-feign/使用feign进行服务集成.md)

## 使用 ribbon 进行服务集成

[使用 feign 进行服务集成](client-ribbon/使用ribbon进行服务集成.md)

