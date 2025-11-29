# Cache Consistency Spring Boot Starter

缓存一致性Spring Boot Starter组件，用于解决分布式系统中的缓存一致性问题。

## 功能特性

- 基于Redis ZSet实现缓存与数据表的依赖关系管理
- 自动版本控制，确保缓存数据的新鲜度
- 支持新鲜度权重配置，优先清理高优先级缓存
- 通过注解方式简化使用
- 异步清理机制，不影响主业务流程
- 提供增强版缓存工具类，支持特殊业务操作

## 设计原理

1. 通过 Redis 中的 ZSET 维护缓存和数据表的依赖关系，ZSET 中的 key 为具体的表名，value 为具体的业务名称，SCORE 为当前业务的新鲜度（新鲜度越高的，在清除缓存时优先）
2. 当数据表更新的时候，就能知道当前数据表被哪些业务依赖了
3. 为每个表都维护一个版本号，比如 ZSET 中的 Key 就为"表名:version"。当数据表更新的时候，将版本号+1，然后将旧的 Key 中的 Value 列表（具体的业务）进行批量删除，优先删除新鲜度要求高的业务。此时如果有新的数据需要被缓存，就会先读取对应表的最新版本号，然后往 ZSET 中添加新的业务依赖关系

## 使用方法

### 1. 添加依赖

```xml
<dependency>
    <groupId>top.noaharno</groupId>
    <artifactId>cache-consistency-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置文件

```yaml
cache:
  consistency:
    enabled: true
    version-key-prefix: "cache:version:"
    dependency-key-prefix: "cache:dependency:"
    clean-thread-pool-size: 10
    clean-batch-size: 100

spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    timeout: 2000ms
```

### 3. 在业务方法上添加注解

```java
@Service
public class MenuService {
    
    @CacheConsistency(businessName = "menu", tables = {"t_user", "t_menu"}, freshness = 5)
    public List<Menu> getMenuList() {
        // 业务逻辑
        return menuList;
    }
}
```

### 4. 表更新时触发缓存清理

```java
@Service
public class UserService {
    
    @Autowired
    private TableUpdateEventPublisher eventPublisher;
    
    public void updateUser(User user) {
        // 更新用户表
        userMapper.update(user);
        
        // 发布表更新事件
        eventPublisher.publishTableUpdateEvent("t_user");
    }
}
```

### 5. 使用增强版缓存工具类

```java
@Service
public class BusinessService {
    
    @Autowired
    private EnhancedCacheUtil enhancedCacheUtil;
    
    public String getUserInfo(String userId) {
        String cacheKey = "user:info:" + userId;
        
        // 从缓存中获取数据
        String cachedData = enhancedCacheUtil.getStringRedisTemplate().opsForValue().get(cacheKey);
        if (cachedData != null && !cachedData.isEmpty()) {
            return cachedData;
        }

        // 模拟从数据库获取数据
        String userInfo = simulateDatabaseQuery(userId);
        
        // 使用增强版缓存工具类设置缓存并执行特殊业务操作
        enhancedCacheUtil.setWithBusinessOperation(
            cacheKey, 
            userInfo, 
            "user-service",  // 业务ID
            "user",          // 表名
            2                // 新鲜度权重
        );
        
        return userInfo;
    }
}
```

## 配置项说明

| 配置项 | 默认值 | 说明 |
| --- | --- | --- |
| cache.consistency.enabled | true | 是否启用缓存一致性功能 |
| cache.consistency.version-key-prefix | "cache:version:" | 版本key的前缀 |
| cache.consistency.dependency-key-prefix | "cache:dependency:" | 依赖关系key的前缀 |
| cache.consistency.clean-thread-pool-size | 10 | 异步清理缓存的线程池大小 |
| cache.consistency.clean-batch-size | 100 | 缓存清理的批处理大小 |

## 核心组件

- `@CacheConsistency`: 注解，用于标记需要参与缓存一致性管理的业务方法
- `CacheVersionService`: 缓存版本服务，负责管理各个表的版本号
- `CacheDependencyService`: 缓存依赖服务，负责管理缓存与数据表之间的依赖关系
- `TableUpdateListener`: 表更新监听器，处理表数据变更时的缓存清理逻辑
- `TableUpdateEventPublisher`: 表更新事件发布器，用于发布表更新事件
- `EnhancedCacheUtil`: 增强版缓存工具类，封装StringRedisTemplate并提供特殊业务操作功能

## 工作流程

1. 业务方法执行时，通过@CacheConsistency注解建立缓存依赖关系
2. 当数据表更新时，调用TableUpdateEventPublisher发布事件
3. TableUpdateListener监听到事件后，获取旧版本的所有依赖业务
4. 异步清理这些业务的缓存
5. 清理旧版本的依赖关系

## 注意事项

1. 需要配置Redis连接信息
2. 需要启用Spring的异步处理功能(@EnableAsync)
3. 业务方法需要是Spring管理的Bean