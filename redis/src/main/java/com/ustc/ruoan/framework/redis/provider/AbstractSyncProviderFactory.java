package com.ustc.ruoan.framework.redis.provider;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ruoan
 * @date 2022/4/4 10:11 下午
 */
public abstract class AbstractSyncProviderFactory<T> {
    private final Map<String, T> providers = new ConcurrentHashMap<>();

    protected T doGetProvider(String cluster) {
        if (StringUtils.isEmpty(cluster)) {
            throw new IllegalArgumentException("cluster name can't be null or empty");
        }
        T provider = providers.get(cluster);
        if (provider == null) {
            synchronized (this) {
                provider = providers.get(cluster);
                if (provider == null) {
                    provider = createProvider(cluster);
                    providers.put(cluster, provider);
                }
            }
        }
        return provider;
    }

    /**
     * 创建单例的redis-client
     *
     * @param cluster redis 集群名称
     * @return 单例的redis-client
     */
    protected abstract T createProvider(String cluster);
}
