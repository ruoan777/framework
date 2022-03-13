package com.ustc.ruoan.ruoandebug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author ruoan
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class RuoanDebugApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuoanDebugApplication.class, args);
    }

}
