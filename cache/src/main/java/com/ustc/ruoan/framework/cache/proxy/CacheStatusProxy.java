package com.ustc.ruoan.framework.cache.proxy;

import com.ustc.ruoan.framework.cache.Cache;
import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.manage.CacheStats;

import java.util.*;

/**
 * 缓存静态代理
 *
 * @author ruoan
 * @date 2022/4/3 10:43 下午
 */
public class CacheStatusProxy<K, V> implements Cache<K, V> {

    private Cache<K, V> cache;
    private CacheStats<K, V> stats;

    public CacheStatusProxy(String name, CacheType type, Cache<K, V> cache) {
        this.cache = cache;
        this.stats = new CacheStats<>(name, type, cache);
    }

    @Override
    public CacheType cacheType() {
        return this.cache.cacheType();
    }

    @Override
    public String cacheName() {
        return this.cache.cacheName();
    }

    @Override
    public V get(K key) {
        return this.cache.get(key);
    }

    @Override
    public Set<K> keys() {
        return this.cache.keys();
    }

    @Override
    public boolean containsKey(K key) {
        return this.cache.containsKey(key);
    }

    @Override
    public Map<K, V> getAll(Collection<K> keys) {
        return this.cache.getAll(keys);
    }

    @Override
    public Map<K, V> asMap() {
        return this.cache.asMap();
    }

    @Override
    public List<String> getChangeFactors() {
        return this.cache.getChangeFactors();
    }

    @Override
    public boolean put(K key, V value) {
        return this.cache.put(key, value);
    }

    @Override
    public void putAll(Map<K, V> values) {
        this.cache.putAll(values);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    public void clear(K key) {
        this.cache.clear(key);
    }

    @Override
    public void clearAll(Collection<K> keys) {
        this.cache.clearAll(keys);
    }

    @Override
    public Date refreshDate() {
        return this.cache.refreshDate();
    }

    @Override
    public int expiryMillis() {
        return this.cache.expiryMillis();
    }

    @Override
    public int maxCount() {
        return this.cache.maxCount();
    }

    @Override
    public double hitRate() {
        return this.cache.hitRate();
    }

    @Override
    public long hitCount() {
        return this.cache.hitCount();
    }

    @Override
    public long missCount() {
        return this.cache.missCount();
    }
}
