package com.ustc.ruoan.framework.redis.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

/**
 * @author ruoan
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class RedisCacheProvider {

    /**
     * 模拟redis client
     */
    private String name;

    public String get(String key) {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return name.concat("_").concat(key);
    }

    public Boolean exists(String cacheKey) {
        return true;
    }
}
