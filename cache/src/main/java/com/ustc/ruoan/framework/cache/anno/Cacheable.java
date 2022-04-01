package com.ustc.ruoan.framework.cache.anno;

import com.ustc.ruoan.framework.cache.generator.DefaultKeyGenerator;
import com.ustc.ruoan.framework.cache.generator.KeyGenerator;

import java.lang.annotation.*;

/**
 * @author ruoan
 * @date 2022/4/1 9:39 下午
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {

    String name() default "";

    String key() default "";

    CacheType type() default CacheType.MEM;

    String[] options() default "";

    String[] changeFactors() default "";

    CacheValueType cacheValueType() default CacheValueType.CACHE_EMPTY;

    int expiryMillis() default -1;

    int maxCount() default 0;

    boolean withPrefix() default true;

    Class<? extends KeyGenerator> generator() default DefaultKeyGenerator.class;
}