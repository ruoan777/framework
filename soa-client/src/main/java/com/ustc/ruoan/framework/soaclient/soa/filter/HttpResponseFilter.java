package com.ustc.ruoan.framework.soaclient.soa.filter;

import org.apache.http.HttpResponse;

/**
 * @author ruoan
 * @date 2022/3/23 11:38 下午
 */
public interface HttpResponseFilter {

    /**
     * 响应流过滤器
     *
     * @param response 原生响应流
     */
    void apply(HttpResponse response);
}
