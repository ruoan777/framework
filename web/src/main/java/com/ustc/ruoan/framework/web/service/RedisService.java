package com.ustc.ruoan.framework.web.service;

import com.ustc.ruoan.framework.cache.Cache;
import com.ustc.ruoan.framework.cache.anno.Cacheable;
import com.ustc.ruoan.framework.redis.ano.RedisProvider;
import com.ustc.ruoan.framework.redis.provider.RedisCacheProvider;
import com.ustc.ruoan.framework.soaclient.soa.hello.HelloRequestType;
import com.ustc.ruoan.framework.soaclient.soa.hello.HelloResponseType;
import com.ustc.ruoan.framework.web.anno.MethodLog;
import com.ustc.ruoan.framework.web.proxy.Proxy;
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
    private Proxy proxy;

    @Cacheable
    private Cache cache;

    @MethodLog
    public String get(String key) throws Exception {
        return self.anotherGet(key);
    }

    @Cacheable
    public String anotherGet(String key) throws Exception {
        RedisService redisService = (RedisService) AopContext.currentProxy();
        HelloResponseType hello = hello(redisCacheProvider.get(key));
        return hello.getMsg();
    }

    public HelloResponseType hello(String name) throws Exception {
        HelloRequestType helloRequestType = new HelloRequestType();
        helloRequestType.setName(name);
        helloRequestType.setAge(18);
        return proxy.execute(helloRequestType);
    }
}
