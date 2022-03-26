package com.ustc.ruoan.framework.soaclient.invoker;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * @author ruoan
 * @date 2022/3/20 11:49 下午
 */
public interface ClientInvoker {

    /**
     * url 直接调用
     */
    <TReq, TResp> TResp invokeDirect(String operation, TReq request, String url, Class<TResp> responseClass) throws Exception;

    /**
     * 直接调用
     */
    <TReq, TResp> TResp invoke(String operation, TReq request, Class<TResp> responseClass) throws Exception;

    /**
     * 回调
     */
    <TReq, TResp> TResp invokeFallback(String operation, TReq request, Object fallbackProvider, Class<TResp> responseClass) throws Exception;

    /**
     * 异步
     */
    <TReq, TResp> ListenableFuture<TResp> invokeAsync(String operation, TReq request, Class<TResp> respClass) throws Exception;

    void setRequestConfig(String operation, ClientConfig clientConfig);
}
