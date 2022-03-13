package com.ustc.ruoan.framework.web.configuration;

import com.ustc.ruoan.framework.web.interceptor.ContextInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ruoan
 */
@Configuration
public class ContextInterceptorAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ContextInterceptor())
                .addPathPatterns("/core/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login");
    }
}
