package com.ustc.ruoan.framework.soaclient.invoker;

import com.ustc.ruoan.framework.soaclient.soa.ServiceClientBase;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ruoan
 * @date 2022/3/20 11:36 下午
 */
public class ClientInvokerProxy implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final Map<Class<?>, ClientInvoker> invokers;
    private final Map<Method, ClientInvokerDescriptor> clients;

    public ClientInvokerProxy() {
        super();
        this.clients = new ConcurrentHashMap<>();
        this.invokers = new ConcurrentHashMap<>();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Object invoke(Method method, Object[] args) throws Exception {
        ClientInvokerDescriptor descriptor = getInvoker(method);
        Object request = args[0];
        ClientInvoker invoker = descriptor.getClientInvoker();
        invoker.setRequestConfig(descriptor.getName(), descriptor.getClientConfig());

        switch (descriptor.getType()) {
            case async:
                return invoker.invokeAsync(descriptor.getName(), request, descriptor.getResponseClass());
            case fallback:
                return invoker.invokeFallback(descriptor.getName(), request, args[1], descriptor.getResponseClass());
            case direct:
                return invoker.invokeDirect(descriptor.getName(), request, (String) args[1], descriptor.getResponseClass());
            case Default:
            default:
                return invoker.invoke(descriptor.getName(), request, descriptor.getResponseClass());
        }
    }

    private ClientInvokerDescriptor getInvoker(Method method) {
        if (!clients.containsKey(method)) {
            ClientInvokerDescriptor descriptor = new ClientInvokerDescriptor(method);
            descriptor.setClientInvoker(getClientInvoker(descriptor));
            clients.put(method, descriptor);
        }
        return clients.get(method);
    }

    private ClientInvoker getClientInvoker(ClientInvokerDescriptor descriptor) {
        Class<?> clientClass = descriptor.getClientClass();
        if (!invokers.containsKey(clientClass)) {
            ServiceClientBase<?> client = (ServiceClientBase<?>) this.applicationContext.getBean(clientClass);
            invokers.put(clientClass, new DefaultClientInvoker(client, descriptor.getClientConfig()));
        }
        return invokers.get(clientClass);
    }
}
