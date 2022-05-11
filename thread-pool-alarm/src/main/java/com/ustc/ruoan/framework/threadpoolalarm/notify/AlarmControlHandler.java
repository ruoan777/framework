package com.ustc.ruoan.framework.threadpoolalarm.notify;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.ustc.ruoan.framework.threadpoolalarm.entity.AlarmControlDTO;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyPlatformEnum;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ruoan
 * @date 2022/5/11 11:25 下午
 */
public class AlarmControlHandler {

    private final Map<String, ReentrantLock> threadPoolLock = Maps.newHashMap();

    /**
     * 缓存拆分到通知类型维度
     */
    private final Map<String, Cache<NotifyTypeEnum, String>> threadPoolAlarmCache = Maps.newConcurrentMap();

    public boolean isSendAlarm(AlarmControlDTO alarmControl) {
        Cache<NotifyTypeEnum, String> cache = threadPoolAlarmCache.get(alarmControl.buildPk());
        if (Objects.isNull(cache)) {
            return false;
        }
        String pkId = cache.getIfPresent(alarmControl.getTypeEnum());
        if (StringUtils.isNotEmpty(pkId)) {
            return false;
        }
        ReentrantLock lock = threadPoolLock.get(pkId);
        lock.lock();
        try {
            pkId = cache.getIfPresent(alarmControl.getTypeEnum());
            if (StrUtil.isBlank(pkId)) {
                // Val meaningless.
                cache.put(alarmControl.getTypeEnum(), IdUtil.simpleUUID());
                return true;
            }
        } finally {
            lock.unlock();
        }
        return true;
    }

    public void initCacheAndLock(String threadPoolId, NotifyPlatformEnum platform, Integer interval) {
        String threadPoolKey = StrUtil.builder(threadPoolId, "+", platform.toString()).toString();
        Cache<NotifyTypeEnum, String> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(interval, TimeUnit.MINUTES)
                .build();
        threadPoolAlarmCache.put(threadPoolKey, cache);

        ReentrantLock reentrantLock = new ReentrantLock();
        threadPoolLock.put(threadPoolKey, reentrantLock);
    }
}
