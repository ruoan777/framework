package com.ustc.ruoan.framework.cache.generator;

/**
 * @author ruoan
 * @date 2022/4/1 9:51 下午
 */
public abstract class KeyGenerator {

    public static final String LINK = "_";

    public String getKey(String key, Class<?>[] parameterTypes, Object[] arguments) {
        StringBuilder sb = new StringBuilder();
        key = buildKey(key, parameterTypes, arguments);
        sb.append(key);
        return sb.toString();
    }

    /**
     * 用来自定义缓存key的构造规则
     *
     * @param key            注解key
     * @param parameterTypes 参数Class类型
     * @param arguments      参数
     * @return 构造的key
     */
    public abstract String buildKey(String key, Class<?>[] parameterTypes, Object[] arguments);
}
