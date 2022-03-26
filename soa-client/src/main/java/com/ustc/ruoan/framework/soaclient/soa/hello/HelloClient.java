package com.ustc.ruoan.framework.soaclient.soa.hello;

import com.google.common.util.concurrent.ListenableFuture;
import com.ustc.ruoan.framework.soaclient.soa.ServiceClientBase;

/**
 * @author ruoan
 * @date 2022/3/21 12:09 上午
 */
public class HelloClient extends ServiceClientBase<HelloClient> implements HelloClientInterface {

    private HelloClient(String serviceName, String serviceNamespace, String subEnv) {
        super(HelloClient.class, serviceName, serviceNamespace, subEnv);
    }

    public static HelloClient getInstance() {
        return ServiceClientBase.getInstance(HelloClient.class);
    }

    public static HelloClient getInstance(String baseUrl) {
        return ServiceClientBase.getInstance(HelloClient.class, baseUrl);
    }

    @Override
    public CheckHealthResponseType checkHealth(CheckHealthRequestType requestType) throws Exception {
        return super.invoke("checkHealth", requestType, CheckHealthResponseType.class);
    }

    @Override
    public CheckHealthResponseType checkHealth(CheckHealthRequestType requestType, Func<CheckHealthResponseType> fallbackProvider) throws Exception {
        return super.invoke("checkHealth", requestType, fallbackProvider, CheckHealthResponseType.class);
    }

    @Override
    public ListenableFuture<CheckHealthResponseType> checkHealthAsync(CheckHealthRequestType requestType) throws Exception {
        return super.invokeAsync("checkHealth", requestType, CheckHealthResponseType.class);
    }

    @Override
    public HelloResponseType hello(HelloRequestType requestType) throws Exception {
        return super.invoke("hello", requestType, HelloResponseType.class);
    }

    @Override
    public HelloResponseType hello(HelloRequestType requestType, Func<HelloResponseType> fallbackProvider) throws Exception {
        return super.invoke("hello", requestType, fallbackProvider, HelloResponseType.class);
    }

    @Override
    public ListenableFuture<HelloResponseType> helloAsync(HelloRequestType requestType) throws Exception {
        return super.invokeAsync("hello", requestType, HelloResponseType.class);
    }
}
