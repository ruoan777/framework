package com.ustc.ruoan.ruoandebug.service;

import com.ustc.ruoan.ruoandebug.anno.MethodLog;
import com.ustc.ruoan.ruoandebug.anno.RedisProvider;
import com.ustc.ruoan.ruoandebug.redis.CacheProvider;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ruoan
 */
@Component
public class RedisService {

    @RedisProvider("hello")
    private CacheProvider cacheProvider;

    @Autowired
    private RedisService redisService;

    @MethodLog
    public String get(String key) throws InterruptedException {
        String s = ((RedisService) AopContext.currentProxy()).get(key);
        redisService.anotherGet(key);
        return anotherGet(key);
    }

    @MethodLog
    public String anotherGet(String key) throws InterruptedException {
        return cacheProvider.get(key);
    }
}
