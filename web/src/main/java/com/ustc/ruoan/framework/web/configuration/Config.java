package com.ustc.ruoan.framework.web.configuration;

import com.ustc.ruoan.framework.web.aspect.RedisInitializingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ruoan
 */
@Configuration
public class Config {

    @Bean
    public RedisInitializingAspect redisInitializing() {
        return new RedisInitializingAspect();
    }

}