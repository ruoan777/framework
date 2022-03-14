package com.ustc.ruoan.framework.web.service;

import com.ustc.ruoan.framework.redis.ano.RedisProvider;
import com.ustc.ruoan.framework.redis.provider.CacheProvider;
import com.ustc.ruoan.framework.web.anno.MethodLog;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

/**
 * @author ruoan
 */
@Component
public class RedisService {

    @RedisProvider("redis_cluster")
    private CacheProvider cacheProvider;

    @MethodLog
    public String get(String key) throws InterruptedException {
        return anotherGet(key);
    }

    @MethodLog
    public String anotherGet(String key) throws InterruptedException {
        RedisService redisService = (RedisService)AopContext.currentProxy();
        return cacheProvider.get(key);
    }
}
