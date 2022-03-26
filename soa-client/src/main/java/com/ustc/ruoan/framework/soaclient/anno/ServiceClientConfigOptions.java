package com.ustc.ruoan.framework.soaclient.anno;

import java.lang.annotation.*;

/**
 * @author ruoan
 * @date 2022/3/20 11:41 上午
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceClientConfigOptions {

    String format() default "";

    int timeout() default 0;

    int socketTimeout() default 0;

    int maxConnectionPerRoute() default 0;

    int connectTimeout() default 0;

    int idleTime() default 0;

    boolean logEnable() default true;

    boolean ignoreError() default true;
}
