package com.ustc.ruoan.framework.cache.autoconfigure;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import sun.jvm.hotspot.HelloWorld;

/**
 * @author ruoan
 * @date 2022/4/1 9:38 下午
 */
@Configuration
public class CacheAutoConfiguration {

    @Bean
    public ServletRegistrationBean<DispatcherServlet> servletRegistrationBean(WebApplicationContext applicationContext) {
        ServletRegistrationBean<DispatcherServlet> servletRegistrationBean = new ServletRegistrationBean<>();
        servletRegistrationBean.setName("cacheDispatcherServlet");
        servletRegistrationBean.setServlet(new DispatcherServlet(applicationContext));
        servletRegistrationBean.addUrlMappings("/cache/*");
        return servletRegistrationBean;
    }

    @Bean
    public HelloWorld helloWorld() {
        System.out.println("CacheAutoConfiguration is registering");
        return null;
    }
}