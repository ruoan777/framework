package com.ustc.ruoan.framework.redis.spring;

import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @author ruoan
 */
public class RedisAutoConfigurationBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return RedisAutoConfigurationBean.class;
    }

    @Override
    protected boolean shouldGenerateId() {
        return true;
    }
}
