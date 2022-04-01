package com.ustc.ruoan.framework.cache;

import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.anno.Cacheable;

import java.lang.reflect.Type;

/**
 * @author ruoan
 * @date 2022/4/1 10:33 下午
 */
public interface CacheProvider {

    /**
     * @see CacheType
     */
    CacheType  getType();

    /**
     * 缓存构建
     */
    <K, V> Cache<K, V> build(String cacheName, Type fieldType, Cacheable cacheable);
}
