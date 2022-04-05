package com.ustc.ruoan.framework.cache.util;

import com.google.common.cache.CacheBuilder;
import com.ustc.ruoan.framework.cache.Cache;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author ruoan
 * @date 2022/4/5 9:08 下午
 */
public class LockUtil {

    private static final Object DEFAULT_LOCK_OBJ_FOR_OBJ = new Object();

    private static final com.google.common.cache.Cache<String, Serializable> CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build();

    public static Serializable getLockObj(String cacheKey, Cache myLock) {
        Serializable cacheLock = CACHE.getIfPresent(cacheKey);
        if (cacheLock == null) {
            Object ml = myLock;
            if (ml == null) {
                ml = DEFAULT_LOCK_OBJ_FOR_OBJ;
            }
            synchronized (ml) {
                cacheLock = CACHE.getIfPresent(cacheKey);
                if (cacheLock == null) {
                    cacheLock = new Serializable() {
                        private static final long serialVersionUID = 255956860617836425L;
                    };
                    CACHE.put(cacheKey, cacheLock);
                }
            }
        }
        return cacheLock;
    }
}
