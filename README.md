# spring-boot-cli 是一个SpringBoot项目脚手架，方便快速构建项目

## 模块

```bash
.
├── cli-app         # 主应用模块(启动模块，包括Controller和Service)
├── cli-common      # 公共模块
├── cli-dao         # 数据访问模块
├── cli-framework   # 框架模块
```

## 技术栈

### 核心框架
- Spring Boot 2.7.18
- MyBatis-Plus 3.5.6
- 支持多数据源
- Druid
- MySQL 8.x
- FlyWay
- Knife4j(Swagger 3.0)

### 用户认证与授权
- SaToken

### 中间件
- 缓存中间件
  - Redis 6.x
  - 支持Redis单节点、哨兵和集群模式
- 消息队列
  - RocketMQ 5.x
  - Kafka 3.x
  - 支持启用或禁用消息队列功能
  - 支持选择启用RocketMQ或Kafka消息队列
- MongoDB 6.x

### 工具类
- Lombok
- OkHttp3
- Apache Commons
- Jackson
- Fastjson