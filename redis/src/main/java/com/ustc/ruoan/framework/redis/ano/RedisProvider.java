package com.ustc.ruoan.framework.redis.ano;

import java.lang.annotation.*;

/**
 * @author ruoan
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisProvider {
    String value();
}
