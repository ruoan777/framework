package com.ustc.ruoan.framework.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author ruoan
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class RuoanWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuoanWebApplication.class, args);
    }

}
