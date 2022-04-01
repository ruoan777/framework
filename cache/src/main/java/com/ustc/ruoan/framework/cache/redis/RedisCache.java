package com.ustc.ruoan.framework.cache.redis;

import com.ustc.ruoan.framework.cache.Cache;
import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.util.GzipUtil;
import com.ustc.ruoan.framework.cache.util.JsonUtil;
import com.ustc.ruoan.framework.redis.provider.RedisCacheProvider;
import org.apache.commons.lang3.StringUtils;

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
    private Class<V> type;
    private Set<String> keys;
    private Boolean gzip;
    private List<String> changeFactors;
    private int expiryMillis;
    private Date refreshDate;
    private AtomicLong hitCount;
    private AtomicLong missCount;

    public RedisCache(RedisCacheProvider provider,
                      Class type,
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
        if (gzip) {
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
        Boolean existsInRedis = this.provider.exists(cacheKey);
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
        return null;
    }

    @Override
    public Map<String, V> asMap() {
        return null;
    }

    @Override
    public List<String> getChangeFactors() {
        return null;
    }

    @Override
    public boolean put(String key, V value) {
        return false;
    }

    @Override
    public void putAll(Map<String, V> values) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void clear(String key) {

    }

    @Override
    public void clearAll(Collection<String> keys) {

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
