package com.ustc.ruoan.framework.redis.spring;

import com.ustc.ruoan.framework.redis.configuration.RedisInitializing;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ruoan
 */
public class RedisAutoConfigurationBean implements ApplicationContextAware, ApplicationListener<ApplicationContextEvent> {

    private ApplicationContext applicationContext;

    private static final AtomicBoolean ONLY_INIT_ONCE = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (this.applicationContext != null) {
            if (event instanceof ContextRefreshedEvent && ONLY_INIT_ONCE.compareAndSet(false, true)) {
                new RedisInitializing();
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
