package com.ustc.ruoan.framework.soaclient.anno;

/**
 * @author ruoan
 * @date 2022/3/20 11:41 上午
 */
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
