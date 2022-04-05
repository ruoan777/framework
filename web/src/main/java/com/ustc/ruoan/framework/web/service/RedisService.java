package com.ustc.ruoan.framework.web.service;

import com.ustc.ruoan.framework.cache.Cache;
import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.anno.Cacheable;
import com.ustc.ruoan.framework.redis.ano.RedisProvider;
import com.ustc.ruoan.framework.redis.provider.RedisCacheProvider;
import com.ustc.ruoan.framework.soaclient.soa.hello.HelloResponseType;
import com.ustc.ruoan.framework.web.anno.MethodLog;
import com.ustc.ruoan.framework.web.cache.HelloCache;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ruoan
 */
@Component
public class RedisService {

    @RedisProvider("redis_cluster")
    private RedisCacheProvider redisCacheProvider;

    @Autowired
    private RedisService self;


    @Autowired
    private HelloCache helloCache;

    @Cacheable(name = "helloCache", key = "all", type = CacheType.REDIS, cluster = "hello")
    private Cache cache;

    @MethodLog
    public String get(String key) throws Exception {
        return self.anotherGet(key);
    }

    @MethodLog
    public String anotherGet(String key) throws Exception {
        Object hello1 = cache.get("hello");
        RedisService redisService = (RedisService) AopContext.currentProxy();
        HelloResponseType hello = helloCache.hello(redisCacheProvider.get(key));
        return hello.getMsg();
    }
}
