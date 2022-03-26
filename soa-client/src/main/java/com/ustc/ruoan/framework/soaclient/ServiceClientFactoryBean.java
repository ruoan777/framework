package com.ustc.ruoan.framework.soaclient;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author ruoan
 * @date 2022/3/20 11:21 下午
 */
public class ServiceClientFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> serviceClientInterface;

    public ServiceClientFactoryBean(Class<T> serviceClientInterface) {
        this.serviceClientInterface = serviceClientInterface;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        return (T) MethodUtils.invokeStaticMethod(serviceClientInterface, "getInstance", new Object[0]);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceClientInterface;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
