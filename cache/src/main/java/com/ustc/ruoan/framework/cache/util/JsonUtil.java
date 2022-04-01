package com.ustc.ruoan.framework.cache.util;

import com.alibaba.fastjson.JSON;

/**
 * @author ruoan
 * @date 2022/4/1 10:03 下午
 */
public class JsonUtil {

    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
}
