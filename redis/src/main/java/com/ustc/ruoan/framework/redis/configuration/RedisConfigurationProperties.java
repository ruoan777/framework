package com.ustc.ruoan.framework.redis.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ruoan
 */
@ConfigurationProperties(prefix = RedisConfigurationProperties.PREFIX)
public class RedisConfigurationProperties {

    static final String PREFIX = "ustc.ruoan.redis";

    private boolean enable = false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
