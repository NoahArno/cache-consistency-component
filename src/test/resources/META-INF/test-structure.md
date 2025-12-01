# 测试组件结构说明

## 目录结构

```
src/test/
├── java/
│   └── top/noaharno/cacheconsistency/
│       ├── entity/              # 测试实体类
│       │   ├── User.java
│       │   ├── Order.java
│       │   └── Product.java
│       ├── mapper/              # MyBatis Mapper接口
│       │   ├── UserMapper.java
│       │   ├── OrderMapper.java
│       │   └── ProductMapper.java
│       ├── integration/         # 集成测试类
│       │   ├── CacheConsistencyIntegrationTest.java
│       │   └── CacheConsistencyFunctionalTest.java
│       └── TestApplication.java # 测试启动类
└── resources/
    ├── application-test.yml     # 测试配置文件
    ├── schema.sql               # 数据库表结构定义
    ├── data.sql                 # 初始化测试数据
    └── META-INF/
        └── test-structure.md    # 本说明文件
```

## 三张测试表说明

### 1. 用户表 (users)
- 存储用户基本信息
- 字段：id, name, email, age

### 2. 订单表 (orders)
- 存储用户订单信息
- 字段：id, user_id, product_name, price, quantity

### 3. 产品表 (products)
- 存储产品信息
- 字段：id, name, description, price, stock

## 测试策略

1. **集成测试**：
   - 使用H2内存数据库进行真实的数据库操作
   - 通过MyBatis执行CRUD操作
   - 验证缓存一致性机制

2. **功能测试**：
   - 验证版本号更新机制
   - 验证依赖键清理机制
   - 验证多表操作的一致性

## 配置说明

- 使用H2内存数据库，避免外部依赖
- 自动初始化表结构和测试数据
- 配置了Redis连接用于缓存一致性测试