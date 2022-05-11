package com.ustc.ruoan.framework.threadpoolalarm.util;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author ruoan
 * @date 2022/5/12 12:42 上午
 */
public class JsonUtil {

    private static final JsonFacade JSON_FACADE = new JacksonHandler();

    public static String toJSONString(Object object) {
        if (object == null) {
            return null;
        }

        return JSON_FACADE.toJSONString(object);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        return JSON_FACADE.parseObject(text, clazz);
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        return JSON_FACADE.parseArray(text, clazz);
    }

}
