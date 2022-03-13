package com.ustc.ruoan.framework.web.service;

import com.ustc.ruoan.framework.web.anno.MethodLog;
import com.ustc.ruoan.framework.web.anno.RedisProvider;
import com.ustc.ruoan.framework.web.redis.CacheProvider;
import org.springframework.stereotype.Component;

/**
 * @author ruoan
 */
@Component
public class RedisService {

    @RedisProvider("hello")
    private CacheProvider cacheProvider;

    @MethodLog
    public String get(String key) throws InterruptedException {
        return anotherGet(key);
    }

    @MethodLog
    public String anotherGet(String key) throws InterruptedException {
        return cacheProvider.get(key);
    }
}
