package com.ustc.ruoan.framework.cache.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ThreadFactory;

/**
 * @author ruoan
 * @date 2022/4/4 4:21 下午
 */
public class ThreadPoolUtil {

    public static ThreadFactory createThreadFactory(String threadNamePrefix) {
        return createThreadFactory(threadNamePrefix, true);
    }

    public static ThreadFactory createThreadFactory(String threadNamePrefix, boolean daemon) {
        return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").setDaemon(daemon).build();
    }
}
