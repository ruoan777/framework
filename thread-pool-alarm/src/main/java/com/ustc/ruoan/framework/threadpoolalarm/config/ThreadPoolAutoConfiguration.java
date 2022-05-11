package com.ustc.ruoan.framework.threadpoolalarm.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author ruoan
 * @date 2022/5/8 6:50 下午
 */
@Configuration
@EnableConfigurationProperties(BootstrapProperties.class)
@ConditionalOnProperty(prefix = BootstrapProperties.PREFIX, value = "enable", matchIfMissing = true, havingValue = "true")
@ImportAutoConfiguration(MessageNotifyConfiguration.class)
public class ThreadPoolAutoConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

}
