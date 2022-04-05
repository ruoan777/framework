package com.ustc.ruoan.framework.web.cache;

import com.ustc.ruoan.framework.cache.anno.CacheType;
import com.ustc.ruoan.framework.cache.anno.Cacheable;
import com.ustc.ruoan.framework.cache.manage.CacheConst;
import com.ustc.ruoan.framework.soaclient.soa.hello.HelloRequestType;
import com.ustc.ruoan.framework.soaclient.soa.hello.HelloResponseType;
import com.ustc.ruoan.framework.web.proxy.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author ruoan
 * @date 2022/4/5 1:29 下午
 */
@Repository
public class HelloCache {

    @Autowired
    private Proxy proxy;

    @Cacheable(name = "HelloCache", key = "hello_{1}", expiryMillis = CacheConst.ONE_DAY, type = CacheType.REDIS)
    public HelloResponseType hello(String name) {
        HelloRequestType helloRequestType = new HelloRequestType();
        helloRequestType.setName(name);
        helloRequestType.setAge(18);
        return proxy.execute(helloRequestType);
    }
}
