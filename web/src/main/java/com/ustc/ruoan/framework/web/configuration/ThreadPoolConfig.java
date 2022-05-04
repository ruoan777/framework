package com.ustc.ruoan.framework.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ruoan
 * @date 2022/5/4 3:54 下午
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ScheduledExecutorService getScheduledExecutorService() {
        AtomicInteger poolNum = new AtomicInteger(0);
        return new ScheduledThreadPoolExecutor(2, r -> {
            Thread t = new Thread(r);
            t.setName("LoopLongPollingThread-" + poolNum.incrementAndGet());
            return t;
        });
    }
}
