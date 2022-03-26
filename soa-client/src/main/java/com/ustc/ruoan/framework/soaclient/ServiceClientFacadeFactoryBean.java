package com.ustc.ruoan.framework.soaclient;

import com.ustc.ruoan.framework.soaclient.invoker.ClientInvokerProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

/**
 * @author ruoan
 * @date 2022/3/20 11:26 下午
 */
@Slf4j
public class ServiceClientFacadeFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> serviceClientFacadeInterface;

    public ServiceClientFacadeFactoryBean(Class<T> serviceClientFacadeInterface) {
        this.serviceClientFacadeInterface = serviceClientFacadeInterface;
    }

    @Autowired
    private ClientInvokerProxy invokerProxy;

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{serviceClientFacadeInterface});
        enhancer.setCallback((InvocationHandler) (proxy, method, args) -> {
            if (args.length == 0) {
                return null;
            }
            Object result = null;
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Exception ex = null;
            try {
                result = invokerProxy.invoke(method, args);
                return result;
            } catch (Exception e) {
                ex = e;
                return null;
            } finally {
                logging(method, args[0], result, stopWatch.getTotalTimeSeconds(), ex);
            }
        });
        return (T) enhancer.create();
    }

    private void logging(Method method, Object arg, Object result, double totalTimeSeconds, Exception ex) {
    }

    @Override
    public Class<?> getObjectType() {
        return serviceClientFacadeInterface;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
