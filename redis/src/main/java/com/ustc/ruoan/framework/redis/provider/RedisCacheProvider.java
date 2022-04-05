package com.ustc.ruoan.framework.redis.provider;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ruoan
 */
@Setter
@Getter
@ToString
public class RedisCacheProvider {

    /**
     * 模拟redis client
     */
    private static Map<String, String> mockRedisClient = new HashMap<>();

    private String name;

    RedisCacheProvider(String name) {
        this.name = name;
        init();
    }

    /**
     * put some dummy k-v for test
     */
    private void init() {
        mockRedisClient.put("hello", "world");
        mockRedisClient.put("name", "ruoan");
    }

    public String get(String key) {
        return mockRedisClient.get(key);
    }

    public boolean exists(String key) {
        return mockRedisClient.containsKey(key);
    }

    public boolean set(String key, String value) {
        mockRedisClient.put(key, value);
        return true;
    }

    public boolean expire(String key, int seconds) {
        return true;
    }

    public void del(String[] allKeys) {
        Arrays.stream(allKeys).forEach(x -> mockRedisClient.remove(x));
    }

    public void del(String key) {
        mockRedisClient.remove(key);
    }

    public Map<String, String> hgetAll(String key) {
        return new HashMap<>();
    }

    public boolean set(String lockKey, String s, String nx, String ex, int i) {
        return true;
    }
}
