package com.ustc.ruoan.framework.cache.anno;

import com.ustc.ruoan.framework.cache.autoconfigure.CacheAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ruoan
 * @date 2022/4/1 10:22 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CacheAutoConfiguration.class)
public @interface EnableCaching {

    String value() default "";

}
