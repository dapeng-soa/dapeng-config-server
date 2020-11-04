# dapeng-config-server
大鹏配置中心
文档:[使用配置中心](https://dapeng-soa.github.io/docs/zh/config-server)

## 数据库初始化
- [config_server_db_init.sql](https://github.com/dapeng-soa/dapeng-config-server/blob/master/sql/1_config_server_db_init.sql)
- [user_info_data_init.sql](https://github.com/dapeng-soa/dapeng-config-server/blob/master/sql/2_user_info_data_init.sql)

## docker-compose

> configServer.yml

```yml
version: '2'
services:
  configServer:
    image: dapengsoa/dapeng-config-server:2.2.1
    container_name: configServer
    restart: on-failure:3
    stop_grace_period: 30s
    environment:
      - serviceName=configServer
      - deploy_socket_url=127.0.0.1:6886
      - build_enable=true
      - TZ=CST-8
      - soa_zookeeper_host=127.0.0.1:2181
      - JAVA_OPTS= -Dname=configServer -Xms512M -Xmx512M -Xss256K -Dfile.encoding=UTF-8 -Dsun.jun.encoding=UTF-8
    ports:
      - "6800:8080"
    volumes:
      - "~/data/logs/dapeng-config-server/:/data/logs/"
```
> 其中 `deploy_socket_url` 为 [cs-agent-server](https://github.com/dapeng-soa/cs-agent-server) 启动地址

## 启动配置中心
```
docker-compose -f configServer.yml up -d
```
