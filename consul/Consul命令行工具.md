# Consul命令行工具

所需端口：

| 使用                           | 默认端口         |
|-------------------------------|------------------|
| DNS: The DNS server           | 8600             |
| HTTP: The HTTP API            | 8500             |
| HTTPS: The HTTPs API          | disabled (8501)* |
| gRPC: The gRPC API            | disabled (8502)* |
| LAN Serf: The Serf LAN port.  | 8301             |
| Wan Serf: The Serf WAN port   | 8302             |
| server: Server RPC address    | 8300             |
| Sidecar Proxy Min: Inclusive min port number to use for automatically assigned sidecar service registrations. | 21000 |
| Sidecar Proxy Max: Inclusive max port number to use for automatically assigned sidecar service registrations. | 21255 |

* 对于HTTPS和gRPC表中指定的端口是推荐。
* 请注意，可以在代理配置中更改默认端口。

## 启动 Consul agent

```sh
consul agent -data-dir=/tmp/consul
```

```log
==> Found address '172.17.0.4' for interface 'eth0', setting bind option...
==> Starting Consul agent...
==> Consul agent running!
           Version: 'v1.4.4'
           Node ID: '932ece78-b1ce-2cd8-9dd8-9abb68891911'
         Node name: '14faa40d8981'
        Datacenter: 'dc1' (Segment: '<all>')
            Server: true (Bootstrap: false)
       Client Addr: [0.0.0.0] (HTTP: 8500, HTTPS: -1, gRPC: 8502, DNS: 8600)
      Cluster Addr: 172.17.0.4 (LAN: 8301, WAN: 8302)
           Encrypt: Gossip: false, TLS-Outgoing: false, TLS-Incoming: false
```

## 停止 Consul agent