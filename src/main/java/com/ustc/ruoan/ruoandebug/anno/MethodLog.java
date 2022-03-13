package com.ustc.ruoan.ruoandebug.anno;

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