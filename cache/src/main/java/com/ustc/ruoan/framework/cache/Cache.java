package com.ustc.ruoan.framework.cache;

import com.ustc.ruoan.framework.cache.anno.CacheType;

import java.util.*;

/**
 * @author ruoan
 * @date 2022/4/1 10:33 下午
 */
public interface Cache<K, V> {

    /**
     * @see CacheType
     */
    CacheType cacheType();

    String cacheName();

    V get(K key);

    Set<K> keys();

    boolean containsKey(K key);

    Map<K, V> getAll(Collection<K> keys);

    Map<K, V> asMap();

    List<String> getChangeFactors();

    boolean put(K key, V value);

    void putAll(Map<K, V> values);

    void clear();

    void clear(K key);

    void clearAll(Collection<K> keys);

    Date refreshDate();

    int expiryMillis();

    int maxCount();

    double hitRate();

    long hitCount();

    long missCount();
}
