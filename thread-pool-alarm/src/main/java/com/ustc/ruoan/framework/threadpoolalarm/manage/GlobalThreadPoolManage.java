package com.ustc.ruoan.framework.threadpoolalarm.manage;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ruoan
 * @date 2022/5/9 12:07 上午
 */
public class GlobalThreadPoolManage {

    private static final Map<String, ThreadPoolExecutor> EXECUTOR_MAP = new ConcurrentHashMap();

    public static List<String> listThreadPoolId() {
        return Lists.newArrayList(EXECUTOR_MAP.keySet());
    }

    public static Integer getThreadPoolNum() {
        return listThreadPoolId().size();
    }


    public static ThreadPoolExecutor getExecutorService(String threadPoolId) {
        return EXECUTOR_MAP.get(threadPoolId);
    }

    public static void registerPool(String threadPoolId, ThreadPoolExecutor executor) {
        EXECUTOR_MAP.put(threadPoolId, executor);
    }

}
