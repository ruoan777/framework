package com.ustc.ruoan.framework.cache;

import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.anno.Cacheable;
import com.ustc.ruoan.framework.cache.proxy.CacheStatusProxy;
import com.ustc.ruoan.framework.cache.util.ThreadPoolUtil;
import com.ustc.ruoan.framework.redis.provider.RedisCacheProvider;
import com.ustc.ruoan.framework.redis.provider.RedisFactory;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ruoan
 * @date 2022/4/3 10:51 下午
 */
public class CacheFactory implements InitializingBean, ApplicationContextAware {

    private static final String BASE_PACKAGE = "com.ustc.ruoan";

    private ApplicationContext applicationContext;

    ScheduledExecutorService refreshCacheDataExecutor = Executors.newScheduledThreadPool(1,
            ThreadPoolUtil.createThreadFactory("RefreshCacheData-Pool"));

    ScheduledExecutorService refreshCacheKeyExecutor = Executors.newScheduledThreadPool(1,
            ThreadPoolUtil.createThreadFactory("RefreshCacheKey-Pool"));


    private Map<CacheType, CacheProvider> cacheProviderMap;
    private Map<String, Cache> caches;
    private RedisCacheProvider redisCacheProvider;

    public CacheFactory() {
        this.cacheProviderMap = new ConcurrentHashMap<>();
        this.caches = new ConcurrentHashMap<>();
        this.redisCacheProvider = RedisFactory.getProvider("redis_create_by_cache");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.registerCrracheProviders();
        this.injectCaches();
        this.startRefreshDataSchedule();
        this.startRefreshCacheKeySchedule();
    }

    private void registerCrracheProviders() {
        Map<String, CacheProvider> cacheProviderMap = this.applicationContext.getBeansOfType(CacheProvider.class);
        for (Map.Entry<String, CacheProvider> entry : cacheProviderMap.entrySet()) {
            CacheProvider cacheProvider = entry.getValue();
            this.cacheProviderMap.put(cacheProvider.getType(), cacheProvider);
        }
    }

    private void injectCaches() throws IllegalAccessException {
        Reflections fieldScanner = new Reflections(BASE_PACKAGE, new FieldAnnotationsScanner());
        Set<Field> fieldsAnnotatedWith = fieldScanner.getFieldsAnnotatedWith(Cacheable.class);
        //标注于方法之上的注解不用在这个阶段扫描进去 这里只是示例
        Reflections methodScanner = new Reflections(BASE_PACKAGE, new MethodAnnotationsScanner());
        Set<Method> methodsAnnotatedWith = methodScanner.getMethodsAnnotatedWith(Cacheable.class);
        for (Field field : fieldsAnnotatedWith) {
            Map<String, ?> beansOfType = this.applicationContext.getBeansOfType(field.getDeclaringClass());
            if (beansOfType.isEmpty()) {
                continue;
            }
            Object target = beansOfType.values().iterator().next();
            Object originTarget = AopProxyUtils.getSingletonTarget(target);
            Cacheable cacheable = field.getAnnotation(Cacheable.class);
            ReflectionUtils.makeAccessible(field);
            field.set(originTarget, createCache(cacheable, field.getGenericType()));
        }
    }

    private synchronized Cache<?, ?> createCache(Cacheable cacheable, Type type) {
        if (this.caches.containsKey(cacheable.name())) {
            return this.caches.get(cacheable.name());
        }
        if (this.cacheProviderMap.containsKey(cacheable.type())) {
            CacheProvider cacheProvider = this.cacheProviderMap.get(cacheable.type());
            Cache<Object, Object> cache = cacheProvider.build(cacheable.name(), type, cacheable);
            CacheStatusProxy<?, ?> proxy = new CacheStatusProxy<>(cacheable.name(), cacheable.type(), cache);
            this.caches.put(cacheable.name(), proxy);
            return proxy;
        }
        throw new IllegalArgumentException("Not Found The Cache Provider [" + cacheable.type() + "]");
    }

    /**
     * 消费由binLog推送的数据更新消息
     */
    private void startRefreshDataSchedule() {
        refreshCacheDataExecutor.scheduleWithFixedDelay(() -> {
            Map<String, String> redisRefreshTime = this.redisCacheProvider.hgetAll("redisRefreshTimeKey");
            if (MapUtils.isNotEmpty(redisRefreshTime) && MapUtils.isNotEmpty(caches)) {
                for (Cache<?, ?> cache : caches.values()) {
                    if (CollectionUtils.isNotEmpty(cache.getChangeFactors()) && cache.getChangeFactors().stream().allMatch(redisRefreshTime::containsKey)) {
                        OptionalLong max = cache.getChangeFactors().stream()
                                .filter(redisRefreshTime::containsKey)
                                .mapToLong(x -> Long.parseLong(redisRefreshTime.get(x)))
                                .max();
                        if (max.isPresent() && cache.refreshDate().getTime() < max.getAsLong()) {
                            cache.clear();
                        }
                    }
                }
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    @SuppressWarnings("unchecked")
    private void startRefreshCacheKeySchedule() {
        refreshCacheKeyExecutor.scheduleWithFixedDelay(() -> {
            caches.forEach((cacheName, cache) -> {
                if (CacheType.REDIS.equals(cache.cacheType())) {
                    cache.keys().forEach(cache::containsKey);
                }
            });
        }, 3, 5, TimeUnit.MINUTES);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
