package com.example.cacheconsistency.config;

import com.example.cacheconsistency.interceptor.TableAnalysisInterceptor;
import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 缓存一致性自动配置类
 * 该类负责自动配置缓存一致性组件所需的所有Bean
 *
 * @author NoahArno
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(CacheConsistencyProperties.class)
public class CacheConsistencyAutoConfiguration {

    @Resource
    private StringRedisTemplate redisTemplate;

    @Resource
    private CacheConsistencyProperties cacheConsistencyProperties;

    @Bean
    public TableAnalysisInterceptor tableAnalysisInterceptor() {
        return new TableAnalysisInterceptor(redisTemplate, cacheConsistencyProperties);
    }
}