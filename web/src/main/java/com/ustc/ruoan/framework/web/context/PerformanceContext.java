package com.ustc.ruoan.framework.web.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ruoan
 */
public class PerformanceContext {

    private static final int CONTEXT_DEFAULT_SIZE = 1 << 6;

    private static final ThreadLocal<Map<String, String>> CONTEXT = ThreadLocal.withInitial(() -> new ConcurrentHashMap<>(CONTEXT_DEFAULT_SIZE));

    /**
     * 获取当前线程记录的所有性能埋点信息
     */
    public static Map<String, String> get() {
        return CONTEXT.get();
    }


    public static void create() {
        get();
    }

    public static <T> void add(String key, T value) {
        if (CONTEXT.get().containsKey(key)) {
            remove(key);
        }
        CONTEXT.get().put(key, String.valueOf(value));
    }

    public static String get(String key) {
        return CONTEXT.get().get(key);
    }

    public static String remove(String key) {
        return CONTEXT.get().remove(key);
    }

    public static void clear() {
        CONTEXT.get().clear();
    }

    public static void release() {
        CONTEXT.remove();
    }
}