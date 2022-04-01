package com.ustc.ruoan.framework.web;

import com.ustc.ruoan.framework.cache.anno.EnableCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author ruoan
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableCaching
public class RuoanWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuoanWebApplication.class, args);
    }

}
