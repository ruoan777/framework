package com.ustc.ruoan.framework.redis.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author ruoan
 */
public class RedisAutoConfigurationNameSpaceHandle extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("driven", new RedisAutoConfigurationBeanDefinitionParser());
    }
}
