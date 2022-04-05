package com.ustc.ruoan.framework.cache.proxy;

import com.ustc.ruoan.framework.cache.Cache;
import com.ustc.ruoan.framework.cache.CacheFactory;
import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.anno.CacheValueType;
import com.ustc.ruoan.framework.cache.anno.Cacheable;
import com.ustc.ruoan.framework.cache.generator.DefaultKeyGenerator;
import com.ustc.ruoan.framework.cache.generator.KeyGenerator;
import com.ustc.ruoan.framework.cache.manage.CacheConst;
import com.ustc.ruoan.framework.cache.util.LockUtil;
import com.ustc.ruoan.framework.redis.provider.RedisCacheProvider;
import com.ustc.ruoan.framework.redis.provider.RedisFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ruoan
 * @date 2022/4/4 11:55 下午
 */
@Aspect
@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public class CacheAspectProxy {

    @Autowired
    private CacheFactory cacheFactory;

    @Autowired
    private DefaultKeyGenerator defaultKeyGenerator;
    private final Map<String, KeyGenerator> generatorMap = new ConcurrentHashMap<>();

    private final RedisCacheProvider redisCacheProvider = RedisFactory.getProvider(CacheConst.DEFAULT_REDIS_CLUSTER);

    @Pointcut("@annotation(com.ustc.ruoan.framework.cache.anno.Cacheable)")
    public void pointCutMethod() {
    }

    @Around("pointCutMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type genericReturnType = method.getGenericReturnType();

        Cache<String, ?> cache = getCache(pjp, cacheable, genericReturnType);
        String cacheKey = getKey(pjp, cacheable, parameterTypes);
        if (StringUtils.isEmpty(cacheKey)) {
            return pjp.proceed();
        }
        if (cache.containsKey(cacheKey)) {
            Object result = cache.get(cacheKey);
            if (result != null) {
                return result;
            }
        }
        return getResult(pjp, cacheable, cache, cacheKey);
    }

    private Object getResult(ProceedingJoinPoint pjp, Cacheable cacheable, Cache cache, String cacheKey) {
        Serializable lockObj = LockUtil.getLockObj(cacheKey, cache);
        // lockObj 可以被其它线程访问到，这里到同步是有意义的
        synchronized (lockObj) {
            String lockKey = cacheKey + "_lock";
            try {
                if (lockAndCheckCache(cache, cacheKey, cacheable.type(), lockKey)) {
                    return cache.get(cacheKey);
                }
                Object result = pjp.proceed();
                putCache(cacheable, cache, cacheKey, result);
                return result;
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                delRedisLockKey(cacheable.type(), lockKey);
            }
        }
        return null;
    }

    private void delRedisLockKey(CacheType cacheType, String lockKey) {
        if (CacheType.REDIS.equals(cacheType)) {
            redisCacheProvider.del(lockKey);
        }
    }

    private void putCache(Cacheable cacheable, Cache cache, String cacheKey, Object result) {
        if (result != null) {
            CacheValueType cacheValueType = cacheable.cacheValueType();
            boolean needPutToCache = cacheValueType == CacheValueType.CACHE_EMPTY ||
                    (cacheValueType == CacheValueType.CACHE_NOT_EMPTY && notEmpty(result));
            if (needPutToCache) {
                cache.put(cacheKey, result);
            }
        }
    }

    private boolean lockAndCheckCache(Cache cache, String cacheKey, CacheType cacheType, String lockKey) throws InterruptedException {
        if (CacheType.REDIS.equals(cacheType)) {
            if (!tryLock(lockKey)) {
                log.info("NoRedisLock", lockKey);
                int index = 0;
                while (index < 200) {
                    Thread.sleep(100);
                    //等待锁的过程中命中了缓存，直接读取，如果没有命中尝试重新获取锁
                    if (cache.containsKey(cacheKey)) {
                        return true;
                    } else if (tryLock(lockKey)) {
                        return false;
                    }
                    index++;
                }
                log.info("TimeoutRedisLock", lockKey);
            }
        }
        return cache.containsKey(cacheKey);
    }

    private boolean tryLock(String lockKey) {
        return redisCacheProvider.set(lockKey, "1", "NX", "EX", 20);
    }

    private boolean notEmpty(Object result) {
        if (result instanceof List) {
            return CollectionUtils.isNotEmpty((List) result);
        } else if (result instanceof Map) {
            return MapUtils.isNotEmpty((Map) result);
        } else {
            return result != null;
        }
    }

    private String getKey(ProceedingJoinPoint pjp, Cacheable cacheable, Class<?>[] parameterTypes) throws InstantiationException, IllegalAccessException {
        String generatorClsName = cacheable.generator().getName();
        KeyGenerator keyGenerator;
        if (cacheable.generator().equals(DefaultKeyGenerator.class)) {
            keyGenerator = defaultKeyGenerator;
        } else {
            if (generatorMap.containsKey(generatorClsName)) {
                keyGenerator = generatorMap.get(generatorClsName);
            } else {
                keyGenerator = cacheable.generator().newInstance();
                generatorMap.put(generatorClsName, keyGenerator);
            }
        }
        return keyGenerator.getKey(cacheable.key(), parameterTypes, pjp.getArgs());
    }

    private Cache<String, ?> getCache(ProceedingJoinPoint pjp, Cacheable cacheable, Type genericReturnType) {
        String name = cacheable.name();
        if (StringUtils.isEmpty(name)) {
            name = pjp.getSignature().getDeclaringTypeName().concat(".").concat(pjp.getSignature().getName());
        }
        if (cacheFactory.getCaches().containsKey(name)) {
            return cacheFactory.getCaches().get(name);
        }
        return cacheFactory.createCache(cacheable, genericReturnType);
    }
}
