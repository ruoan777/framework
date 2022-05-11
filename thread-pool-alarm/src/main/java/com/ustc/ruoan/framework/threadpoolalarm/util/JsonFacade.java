package com.ustc.ruoan.framework.threadpoolalarm.util;

import java.util.List;

/**
 * @author ruoan
 * @date 2022/5/12 12:41 上午
 */
public interface JsonFacade {

    /**
     * To JSON string.
     *
     * @param object
     * @return
     */
    String toJSONString(Object object);

    /**
     * Parse object.
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T parseObject(String text, Class<T> clazz);

    /**
     * Parse array.
     *
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> parseArray(String text, Class<T> clazz);
}
