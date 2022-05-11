package com.ustc.ruoan.framework.threadpoolalarm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ruoan
 * @date 2022/5/8 7:12 下午
 */
@Getter
@Setter
@ConfigurationProperties(prefix = BootstrapProperties.PREFIX)
public class BootstrapProperties {

    public static final String PREFIX = "ustc.ruoan.thread-pool";

    private Boolean enable = false;

}
