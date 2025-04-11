# 微服务电商平台（Spring Cloud版）

## 📌 项目概述
基于Spring Cloud Alibaba的分布式电商系统，采用微服务架构解决传统单体应用在高并发场景下的扩展性问题。

## 🛠️ 技术栈

### 核心框架
| 技术                   | 用途                     | 版本         |
|----------------------|--------------------------|------------|
| Spring Boot          | 基础开发框架             | 2.7.12     |
| Spring Cloud         | 微服务解决方案           | 2021.0.8   |
| Spring Cloud Alibaba | 阿里微服务生态集成       | 2021.0.5.0 |
| Mybatis              | mybatis plus       | 2021.0.5.0 |

### 基础设施
```mermaid
graph TD
    A[Nacos] -->|服务注册发现| B(微服务集群)
    A -->|配置中心| B
    C[Sentinel] -->|流量控制| B
    D[Seata] -->|分布式事务| B
```
### 基础设施
```mermaid
sequenceDiagram
    用户->>+网关: 登录请求
    网关->>+认证服务: 验证凭证
    认证服务-->>-网关: JWT令牌
    网关-->>-用户: 返回Token
```

