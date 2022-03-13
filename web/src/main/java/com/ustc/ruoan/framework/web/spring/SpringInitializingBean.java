package com.ustc.ruoan.framework.web.spring;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author ruoan
 */
public abstract class SpringInitializingBean implements ApplicationContextAware, InitializingBean {

    protected ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return ApplicationContextInstance.context;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextInstance.setApplicationContext(applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    private static class ApplicationContextInstance {
        private static ApplicationContext context;

        private static ApplicationContext getApplicationContext() {
            return context;
        }

        private static void setApplicationContext(ApplicationContext applicationContext) {
            context = applicationContext;
        }
    }
}
