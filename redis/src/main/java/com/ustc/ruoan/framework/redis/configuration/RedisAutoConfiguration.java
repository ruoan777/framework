package com.ustc.ruoan.framework.redis.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author ruoan
 */
@Configuration
@EnableConfigurationProperties(RedisConfigurationProperties.class)
@ConditionalOnProperty(prefix = RedisConfigurationProperties.PREFIX, name = "enable", havingValue = "true")
public class RedisAutoConfiguration {

    @Bean(value = "ruoan-redis", initMethod = "init")
    @Scope("singleton")
    public RedisInitializing initializing() {
        return new RedisInitializing();
    }

}
