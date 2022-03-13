package com.ustc.ruoan.framework.web.anno;

import java.lang.annotation.*;

/**
 * @author ruoan
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodLog {
    String value() default "";
}