package com.ustc.ruoan.framework.cache.manage;

import com.ustc.ruoan.framework.cache.Cache;
import com.ustc.ruoan.framework.cache.anno.CacheType;

/**
 * @author ruoan
 * @date 2022/4/3 10:49 下午
 */
public class CacheStats<K, V> {

    private String name;
    private CacheType type;
    private Cache<K, V> cache;

    public CacheStats(String name, CacheType type, Cache<K, V> cache) {
        this.name = name;
        this.type = type;
        this.cache = cache;
    }
}
