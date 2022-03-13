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
public class CacheProvider {

    /**
     * 模拟redis client
     */
    private String name;

    public String get(String key) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return name.concat(key);
    }
}
