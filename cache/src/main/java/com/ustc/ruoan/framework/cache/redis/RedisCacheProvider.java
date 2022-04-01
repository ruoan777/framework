package com.ustc.ruoan.framework.cache.redis;

import com.ustc.ruoan.framework.cache.Cache;
import com.ustc.ruoan.framework.cache.CacheProvider;
import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.anno.Cacheable;

import java.lang.reflect.Type;

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
        return null;
    }
}
