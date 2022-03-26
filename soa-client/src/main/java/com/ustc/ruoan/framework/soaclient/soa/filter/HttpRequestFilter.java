package com.ustc.ruoan.framework.soaclient.soa.filter;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author ruoan
 * @date 2022/3/23 11:40 下午
 */
public interface HttpRequestFilter {

    /**
     * 请求体过滤器
     *
     * @param request 原生请求体
     */
    void apply(HttpRequestBase request);
}
