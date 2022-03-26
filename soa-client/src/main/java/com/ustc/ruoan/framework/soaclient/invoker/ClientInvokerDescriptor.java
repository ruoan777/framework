package com.ustc.ruoan.framework.soaclient.invoker;

import com.google.common.reflect.TypeToken;
import com.ustc.ruoan.framework.soaclient.anno.ServiceClient;
import com.ustc.ruoan.framework.soaclient.anno.ServiceClientConfigOptions;
import com.ustc.ruoan.framework.soaclient.soa.hello.Func;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.Future;

/**
 * @author ruoan
 * @date 2022/3/24 11:06 下午
 */
@Setter
@Getter
public class ClientInvokerDescriptor {
    private Method method;
    private ClientConfig clientConfig;
    private Class<?> clientClass;
    private String name;
    private ProxyMethodType type;
    private ClientInvoker clientInvoker;
    private Class<?> responseClass;

    public ClientInvokerDescriptor(Method method) {
        super();
        this.method = method;
        this.clientConfig = new ClientConfig();
        this.build(method);
    }

    private void build(Method method) {
        ServiceClient clientAnnotation = getServiceClient(method);
        this.clientClass = clientAnnotation.value();
        this.name = method.getName();
        this.type = ProxyMethodType.of(method);
        this.responseClass = method.getReturnType();
        if (this.type.isAsync()) {
            this.name = method.getName().substring(0, method.getName().lastIndexOf("Async"));
            this.responseClass = getResponseType(method.getGenericReturnType());
        }
        this.buildClientConfig(method, clientAnnotation);
    }

    private ServiceClient getServiceClient(Method method) {
        ServiceClient clientAnnotation = null;
        if (method.isAnnotationPresent(ServiceClient.class)) {
            clientAnnotation = method.getAnnotation(ServiceClient.class);
        } else if (method.getDeclaringClass().isAnnotationPresent(ServiceClient.class)) {
            clientAnnotation = method.getDeclaringClass().getAnnotation(ServiceClient.class);
        }
        if (clientAnnotation == null) {
            throw new IllegalArgumentException("Please checked the method add @ServiceClient annotation.");
        }
        return clientAnnotation;
    }

    private Class<?> getResponseType(Type returnType) {
        return TypeToken.of(returnType).resolveType(Future.class.getTypeParameters()[0]).getRawType();
    }

    private void buildClientConfig(Method method, ServiceClient serviceClient) {
        ServiceClientConfigOptions configOptions = null;
        if (method.isAnnotationPresent(ServiceClientConfigOptions.class)) {
            configOptions = method.getAnnotation(ServiceClientConfigOptions.class);
        } else if (method.getDeclaringClass().isAnnotationPresent(ServiceClientConfigOptions.class)) {
            configOptions = method.getDeclaringClass().getAnnotation(ServiceClientConfigOptions.class);
        }
        if (configOptions != null) {
            this.clientConfig = new ClientConfig.ClientConfigBuilder()
                    .setFormat(configOptions.format())
                    .setRequestTimeout(configOptions.timeout())
                    .setSocketTimeout(configOptions.socketTimeout())
                    .setMaxConnectionPerRoute(configOptions.maxConnectionPerRoute())
                    .setConnectTimeout(configOptions.connectTimeout())
                    .setIdleTime(configOptions.idleTime())
                    .setLogEnable(configOptions.logEnable())
                    .setIgnoreError(configOptions.ignoreError())
                    .build();
        } else {
            this.clientConfig = new ClientConfig.ClientConfigBuilder()
                    .setFormat(serviceClient.format())
                    .setRequestTimeout(serviceClient.timeout())
                    .setSocketTimeout(serviceClient.socketTimeout())
                    .setMaxConnectionPerRoute(serviceClient.maxConnectionPerRoute())
                    .setConnectTimeout(serviceClient.connectTimeout())
                    .setIdleTime(serviceClient.idleTime())
                    .setLogEnable(serviceClient.logEnable())
                    .setIgnoreError(serviceClient.ignoreError())
                    .build();
        }
    }

    public enum ProxyMethodType {
        /**
         * 默认同步
         */
        Default {
            @Override
            public boolean isDefault() {
                return true;
            }
        },
        /**
         * 异步
         */
        async {
            @Override
            public boolean isAsync() {
                return true;
            }
        },
        /**
         * 直接调用
         */
        direct {
            @Override
            public boolean isDirect() {
                return true;
            }
        },
        /**
         * 回调
         */
        fallback {
            @Override
            public boolean isFallback() {
                return true;
            }
        };

        public boolean isDefault() {
            return false;
        }

        public boolean isAsync() {
            return false;
        }

        public boolean isDirect() {
            return false;
        }

        public boolean isFallback() {
            return false;
        }

        public static ProxyMethodType of(Method method) {
            if (method.getName().endsWith("Async")) {
                return async;
            }
            Class<?>[] types = method.getParameterTypes();
            if (types.length == 2) {
                if (types[1].equals(Func.class)) {
                    return fallback;
                } else if (types[1].equals(String.class)) {
                    return direct;
                }
            }
            return Default;
        }
    }
}
