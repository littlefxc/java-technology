# RocketMQ 使用记录

## 启动

```shell
nohup sh bin/mqnamesrv &

nohup sh bin/mqbroker -n localhost:9876 autoCreateTopicEnable=true &
```

