package com.ustc.ruoan.framework.soaclient.soa;

import com.ustc.ruoan.framework.soaclient.soa.filter.HttpRequestFilter;
import com.ustc.ruoan.framework.soaclient.soa.filter.HttpResponseFilter;
import com.ustc.ruoan.framework.soaclient.soa.formatter.ContentFormatter;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.Map;

/**
 * @author ruoan
 * @date 2022/3/23 6:07 下午
 */
@Setter
@Getter
public class ExecutionContext<TResp> {

    private String serviceKey;

    private String operationName;

    private String lowerCaseOperationName;

    private String operationKey;

    private String format;

    private ContentFormatter contentFormatter;

    private Object request;

    private Class<?> requestClass;

    private Object response;

    private Class<TResp> responseClass;

    private String originalUrl;

    private String url;

    private String host;

    private URI uri;

    private boolean isIpDirectInvocation;

    private long startTime = System.currentTimeMillis();

    private long endTime = 0L;

    private boolean messageLogged;

    private boolean async;

    private Map<String, String> ephemeralHeaders;

    private Map<String, String> overriddenTraceContext;

    private String serviceApp;

    private String serviceIP;

    private String serviceIdc;

    private String caller;

    private long requestSize;

    private String subEnv;

    private boolean isProxyRequest;

    private String proxyHost;

    private boolean isCanaryRequest;

    private String routeId;

    private String routeExtraInfo;

    private HttpRequestFilter ephemeralHttpRequestFilter;

    private HttpResponseFilter ephemeralHttpResponseFilter;

    private HttpRequest httpRequest;

    private HttpResponse httpResponse;

    private String ack;
    private String status;
    private String message;

    ExecutionContext() {
    }
}
