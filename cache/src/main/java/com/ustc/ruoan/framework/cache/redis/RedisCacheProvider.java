package com.ustc.ruoan.framework.cache.redis;

import com.ustc.ruoan.framework.cache.Cache;
import com.ustc.ruoan.framework.cache.CacheProvider;
import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.anno.Cacheable;
import com.ustc.ruoan.framework.redis.provider.RedisFactory;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author ruoan
 * @date 2022/4/1 10:40 下午
 */
public class RedisCacheProvider implements CacheProvider {

    @Override
    public CacheType getType() {
        return CacheType.REDIS;
    }

    @Override
    public <K, V> Cache<K, V> build(String cacheName, Type fieldType, Cacheable cacheable) {
        String[] options = cacheable.options();
        if (options.length == 0) {
            throw new IllegalArgumentException("redis cache setting error. the @InjectCache options in RedisDbName");
        }
        String cluster = cacheable.cluster();
        if (StringUtils.isEmpty(cluster)) {
            throw new IllegalArgumentException("redis cache setting error. the cluster name is required.");
        }
        com.ustc.ruoan.framework.redis.provider.RedisCacheProvider provider = RedisFactory.getProvider(cluster);
        if (provider == null) {
            throw new IllegalArgumentException("the redis (" + cluster + ") not exist error.");
        }
        boolean gzip = Arrays.asList(options).contains("gzip");
        return new RedisCache(provider,
                fieldType,
                cacheName,
                gzip,
                cacheable.expiryMillis(),
                cacheable.name(),
                cacheable.changeFactors(),
                cacheable.withPrefix());
    }
}
