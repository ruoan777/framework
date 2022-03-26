package com.ustc.ruoan.framework.soaclient.soa.hello;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * @author ruoan
 * @date 2022/3/21 12:09 上午
 */
public interface HelloClientInterface {

    /**
     * 健康检查
     */
    CheckHealthResponseType checkHealth(CheckHealthRequestType request) throws Exception;

    /**
     * 健康检查
     */
    CheckHealthResponseType checkHealth(CheckHealthRequestType request, Func<CheckHealthResponseType> fallbackProvider) throws Exception;

    /**
     * 健康检查
     */
    ListenableFuture<CheckHealthResponseType> checkHealthAsync(CheckHealthRequestType request) throws Exception;

    /**
     * hello
     */
    HelloResponseType hello(HelloRequestType request) throws Exception;

    /**
     * hello
     */
    HelloResponseType hello(HelloRequestType request, Func<HelloResponseType> fallbackProvider) throws Exception;

    /**
     * hello
     */
    ListenableFuture<HelloResponseType> helloAsync(HelloRequestType request) throws Exception;


}
