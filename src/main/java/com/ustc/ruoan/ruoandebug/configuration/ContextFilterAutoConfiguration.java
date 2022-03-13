package com.ustc.ruoan.ruoandebug.configuration;

import com.ustc.ruoan.ruoandebug.filter.ContextFilter;
import com.ustc.ruoan.ruoandebug.spring.ContextFilterCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.DispatcherType;

/**
 * @author ruoan
 */
@Configuration
@Conditional(ContextFilterCondition.class)
@ConditionalOnClass(name = "javax.servlet.Filter")
public class ContextFilterAutoConfiguration {

    @Configuration
    @ConditionalOnMissingClass("org.springframework.boot.context.embedded.FilterRegistrationBean")
    static class ConfigurationNew {

        @Bean(name = "FilterRegistrationBeanNew")
        public FilterRegistrationBean<ContextFilter> factory() {
            FilterRegistrationBean<ContextFilter> filter = new FilterRegistrationBean<>();
            filter.setFilter(new ContextFilter());
            filter.setName("cat-filter");
            filter.addUrlPatterns("/*");
            filter.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
            filter.setAsyncSupported(true);
            filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
            return filter;
        }
    }
}
