package top.noaharno.cacheconsistency.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存一致性配置属性类
 * 用于接收application.yml中关于缓存一致性的配置项
 *
 * @author NoahArno
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "cache.consistency")
public class CacheConsistencyProperties {

    /**
     * 是否启用缓存一致性功能
     */
    private boolean enabled = true;

    /**
     * 版本key的前缀，默认为"cache:version:"
     */
    private String versionKeyPrefix = "cache:version:";

    /**
     * 依赖关系key的前缀，默认为"cache:dependency:"
     */
    private String dependencyKeyPrefix = "cache:dependency:";

    /**
     * 异步清理缓存的线程池大小
     */
    private int cleanThreadPoolSize = 10;

    // Getters and Setters

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVersionKeyPrefix() {
        return versionKeyPrefix;
    }

    public void setVersionKeyPrefix(String versionKeyPrefix) {
        this.versionKeyPrefix = versionKeyPrefix;
    }

    public String getDependencyKeyPrefix() {
        return dependencyKeyPrefix;
    }

    public void setDependencyKeyPrefix(String dependencyKeyPrefix) {
        this.dependencyKeyPrefix = dependencyKeyPrefix;
    }

    public int getCleanThreadPoolSize() {
        return cleanThreadPoolSize;
    }

    public void setCleanThreadPoolSize(int cleanThreadPoolSize) {
        this.cleanThreadPoolSize = cleanThreadPoolSize;
    }
}