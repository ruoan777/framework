package com.ustc.ruoan.framework.cache.mem;

import com.ustc.ruoan.framework.cache.Cache;
import com.ustc.ruoan.framework.cache.CacheProvider;
import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.anno.Cacheable;

import java.lang.reflect.Type;

/**
 * @author ruoan
 * @date 2022/4/1 10:41 下午
 */
public class MemCacheProvider implements CacheProvider {

    @Override
    public CacheType getType() {
        return CacheType.MEM;
    }

    @Override
    public <K, V> Cache<K, V> build(String cacheName, Type fieldType, Cacheable cacheable) {
        return null;
    }
}
