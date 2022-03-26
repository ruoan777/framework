package com.ustc.ruoan.framework.soaclient.anno;

import java.lang.annotation.*;

/**
 * @author ruoan
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceClient {

    /**
     * 代理的具体服务
     */
    Class<?> value();

    String format() default "";

    @Deprecated
    Class<?> root() default ServiceClient.class;

    @Deprecated
    String path() default "";

    int timeout() default 0;

    int socketTimeout() default 0;

    int maxConnectionPerRoute() default 0;

    int connectTimeout() default 0;

    int idleTime() default 0;

    boolean logEnable() default false;

    boolean ignoreError() default false;
}
