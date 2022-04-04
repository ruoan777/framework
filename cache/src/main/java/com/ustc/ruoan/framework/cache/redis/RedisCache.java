package com.ustc.ruoan.framework.cache.redis;

import com.ustc.ruoan.framework.cache.Cache;
import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.util.GzipUtil;
import com.ustc.ruoan.framework.cache.util.JsonUtil;
import com.ustc.ruoan.framework.redis.provider.RedisCacheProvider;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ruoan
 * @date 2022/4/1 10:38 下午
 */
public class RedisCache<V> implements Cache<String, V> {

    private static final String REDIS_CACHE_KEY = "ruoan:";
    private final String prefix;

    private RedisCacheProvider provider;
    private String name;
    private Type type;
    private Set<String> keys;
    private Boolean gzip;
    private List<String> changeFactors;
    private int expiryMillis;
    private Date refreshDate;
    private AtomicLong hitCount;
    private AtomicLong missCount;

    public RedisCache(RedisCacheProvider provider,
                      Type type,
                      String prefix,
                      Boolean gzip,
                      int expiryMillis,
                      String name,
                      String[] changeFactors,
                      boolean withPrefix) {
        super();
        this.prefix = withPrefix ? REDIS_CACHE_KEY.concat(prefix).concat(":") : "";
        this.provider = provider;
        this.name = name;
        this.type = type;
        this.keys = new CopyOnWriteArraySet<>();
        this.gzip = gzip;
        this.changeFactors = Arrays.asList(changeFactors);
        this.expiryMillis = expiryMillis;
        this.refreshDate = new Date();
        this.hitCount = new AtomicLong();
        this.missCount = new AtomicLong();
    }

    private String buildKey(String key) {
        if (StringUtils.isNotBlank(this.prefix) && !key.startsWith(this.prefix)) {
            key = this.prefix + key;
        }
        return key;
    }

    @Override
    public CacheType cacheType() {
        return CacheType.REDIS;
    }

    @Override
    public String cacheName() {
        return this.name;
    }

    @Override
    public V get(String key) {
        String cacheKey = buildKey(key);
        String redisContent = this.provider.get(cacheKey);
        if (StringUtils.isEmpty(redisContent)) {
            return null;
        }
        if (this.gzip) {
            redisContent = GzipUtil.uncompressToString(redisContent);
        }
        return JsonUtil.parseObject(redisContent, this.type);
    }

    @Override
    public Set<String> keys() {
        return this.keys;
    }

    @Override
    public boolean containsKey(String key) {
        String cacheKey = buildKey(key);
        boolean existsInRedis = this.provider.exists(cacheKey);
        boolean existsInMem = this.keys.contains(cacheKey);
        //内存有但redis没有 -> 从内存中删除key
        if (existsInMem && !existsInRedis) {
            keys().remove(cacheKey);
        }
        //redis有但内存没有 -> 往内存中添加key
        else if (existsInRedis && !existsInMem) {
            keys().add(cacheKey);
        }
        if (existsInRedis) {
            this.hitCount.getAndDecrement();
        } else {
            this.missCount.getAndIncrement();
        }
        return existsInRedis;
    }

    @Override
    public Map<String, V> getAll(Collection<String> keys) {
        Map<String, V> values = new HashMap<>();
        for (String key : keys) {
            V v = get(key);
            values.put(key, v);
        }
        return values;
    }

    @Override
    public Map<String, V> asMap() {
        return getAll(keys());
    }

    @Override
    public List<String> getChangeFactors() {
        return this.changeFactors;
    }

    @Override
    public boolean put(String key, V value) {
        String buildKey = buildKey(key);
        String content = JsonUtil.toJsonString(value);
        if (this.gzip) {
            content = GzipUtil.compressToString(content);
        }
        boolean success = this.provider.set(buildKey, content);
        if (!success) {
            return false;
        }
        keys().add(buildKey);
        this.refreshDate = new Date();
        if (this.expiryMillis > 0) {
            int seconds = this.expiryMillis / 1000;
            return this.provider.expire(buildKey, seconds);
        }
        return true;
    }

    @Override
    public void putAll(Map<String, V> values) {
        values.forEach(this::put);
        this.refreshDate = new Date();
    }

    @Override
    public void clear() {
        Set<String> keys = getAllRedisKeys();
        String[] allKeys = keys.toArray(new String[0]);
        this.provider.del(allKeys);
        this.refreshDate = new Date();
        this.keys().clear();
    }

    /**
     * 具体实现应该考虑调用redis提供的 scan 方法
     */
    private Set<String> getAllRedisKeys() {
        return keys();
    }

    @Override
    public void clear(String key) {
        String buildKey = buildKey(key);
        this.provider.del(buildKey);
        keys().remove(buildKey);
        this.refreshDate = new Date();
    }

    @Override
    public void clearAll(Collection<String> keys) {
        keys.forEach(this::clear);
    }

    @Override
    public Date refreshDate() {
        return this.refreshDate;
    }

    @Override
    public int expiryMillis() {
        return 0;
    }

    @Override
    public int maxCount() {
        return 0;
    }

    @Override
    public double hitRate() {
        long requestCount = this.hitCount() + this.missCount();
        BigDecimal hitRate = requestCount == 0 ? BigDecimal.ONE : BigDecimal.valueOf(hitCount() / requestCount);
        return hitRate.doubleValue();
    }

    @Override
    public long hitCount() {
        return this.hitCount.longValue();
    }

    @Override
    public long missCount() {
        return this.missCount.longValue();
    }
}
