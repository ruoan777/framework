package com.ustc.ruoan.framework.soaclient.invoker;

import com.google.common.util.concurrent.ListenableFuture;
import com.ustc.ruoan.framework.soaclient.soa.ServiceClientBase;
import com.ustc.ruoan.framework.soaclient.soa.hello.Func;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author ruoan
 * @date 2022/3/20 11:51 下午
 */
public class DefaultClientInvoker implements ClientInvoker {

    private final ServiceClientBase<? extends ServiceClientBase<?>> serviceClient;
    private final ClientConfig clientConfig;

    public DefaultClientInvoker(ServiceClientBase<? extends ServiceClientBase<?>> serviceClient, ClientConfig clientConfig) {
        this.serviceClient = serviceClient;
        this.clientConfig = clientConfig;
        this.initClient(serviceClient, clientConfig);
    }

    @Override
    public <TReq, TResp> TResp invokeDirect(String operation, TReq request, String url, Class<TResp> responseClass) throws Exception {
        return getServiceClient(url).invoke(operation, request, responseClass);
    }

    @Override
    public <TReq, TResp> TResp invoke(String operation, TReq request, Class<TResp> responseClass) throws Exception {
        return serviceClient.invoke(operation, request, responseClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TReq, TResp> TResp invokeFallback(String operation, TReq request, Object fallbackProvider, Class<TResp> responseClass) throws Exception {
        return serviceClient.invoke(operation, request, (Func<TResp>) fallbackProvider, responseClass);
    }

    @Override
    public <TReq, TResp> ListenableFuture<TResp> invokeAsync(String operation, TReq request, Class<TResp> tRespClass) throws Exception {
        return serviceClient.invokeAsync(operation, request, tRespClass);
    }

    @Override
    public void setRequestConfig(String operation, ClientConfig clientConfig) {
        if (clientConfig.getRequestTimeout() > 0) {
            serviceClient.setRequestTimeout(operation, clientConfig.getRequestTimeout());
        }
        if (clientConfig.getSocketTimeout() > 0) {
            serviceClient.setSocketTimeout(operation, clientConfig.getSocketTimeout());
        }
        if (clientConfig.getConnectTimeout() > 0) {
            serviceClient.setConnectTimeout(operation, clientConfig.getConnectTimeout());
        }
    }

    private ServiceClientBase<?> getServiceClient(String url) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ServiceClientBase<?> client = (ServiceClientBase<?>) MethodUtils.invokeStaticMethod(this.serviceClient.getClass(), "getInstance", new Object[]{url});
        if (clientConfig != null) {
            initClient(client, clientConfig);
        }
        return client;
    }

    /**
     * do some init with soa client
     *
     * @param serviceClient target client which need init
     * @param clientConfig  config
     */
    private void initClient(ServiceClientBase<? extends ServiceClientBase<?>> serviceClient, ClientConfig clientConfig) {
        if (clientConfig.getRequestTimeout() > 0) {
            serviceClient.setRequestTimeout(clientConfig.getRequestTimeout());
        }
        if (clientConfig.getFormat() != null && !clientConfig.getFormat().isEmpty()) {
            serviceClient.setFormat(clientConfig.getFormat());
        }
        if (clientConfig.getConnectTimeout() > 0) {
            serviceClient.setConnectTimeout(clientConfig.getConnectTimeout());
        }
        if (clientConfig.getMaxConnectionPerRoute() > 0) {
            serviceClient.setMaxConnectionPerRoute(clientConfig.getMaxConnectionPerRoute());
        }
        if (clientConfig.getIdleTime() > 0) {
            serviceClient.setIdleTime(clientConfig.getIdleTime());
        }
        if (clientConfig.getSocketTimeout() > 0) {
            serviceClient.setSocketTimeout(clientConfig.getSocketTimeout());
        }
        if (clientConfig.isIgnoreError()) {
            serviceClient.setLogCServiceExceptionAsError(false);
            serviceClient.setLogWebExceptionAsError(false);
        }
    }
}
