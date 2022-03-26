package com.ustc.ruoan.framework.soaclient.soa;

import com.google.common.base.Stopwatch;
import com.google.common.net.HttpHeaders;
import com.google.common.util.concurrent.ListenableFuture;
import com.ustc.ruoan.framework.soaclient.soa.filter.HttpRequestFilter;
import com.ustc.ruoan.framework.soaclient.soa.filter.HttpResponseFilter;
import com.ustc.ruoan.framework.soaclient.soa.filter.MessageInspector;
import com.ustc.ruoan.framework.soaclient.soa.formatter.ContentFormatter;
import com.ustc.ruoan.framework.soaclient.soa.hello.Func;
import com.ustc.ruoan.framework.soaclient.soa.hello.HelloResponseType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ruoan
 * @date 2022/3/20 4:15 下午
 */
@Slf4j
public abstract class ServiceClientBase<DerivedClient extends ServiceClientBase<?>> {

    private static final Map<String, ServiceClientBase<?>> _clientCache = new ConcurrentHashMap<>();

    private static HttpRequestFilter _globalHttpRequestFilter;
    private static HttpResponseFilter _globalHttpResponseFilter;

    private HttpRequestFilter _localHttpRequestFilter;
    private HttpResponseFilter _localHttpResponseFilter;

    private boolean _deserializeUseMemoryStream = false;

    private final Map<String, String> _headers = new ConcurrentHashMap<>();

    private final CopyOnWriteArrayList<MessageInspector> requestMessageInspectors = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<MessageInspector> responseMessageInspectors = new CopyOnWriteArrayList<>();

    private CloseableHttpClient _client;

    protected ServiceClientBase(Class<DerivedClient> clientClass, String serviceName, String serviceNamespace, String subEnv) {
        System.out.println("do and get url");
    }

    /**
     * 模拟获取SOA调用的client类
     */
    public static <DerivedClient extends ServiceClientBase<?>> DerivedClient getInstance(Class<DerivedClient> clientClass, String baseUrl) {
        if (null == baseUrl || baseUrl.isEmpty()) {
            throw new IllegalArgumentException("baseUrl can't be null or empty");
        }
        String key = clientClass.getName() + ":" + baseUrl;
        Class<?>[] paramTypes = new Class[]{String.class};
        Object[] paramValues = new Object[]{baseUrl};
        return ServiceClientBase.doGetInstance(clientClass, key, paramTypes, paramValues);
    }

    /**
     * 模拟获取SOA调用的client类
     */
    public static <DerivedClient extends ServiceClientBase<?>> DerivedClient getInstance(Class<DerivedClient> clientClass) {
        String key = clientClass.getName();
        Class<?>[] paramTypes = new Class[]{String.class, String.class, String.class};
        Object[] paramValues = new Object[]{null, null, null};
        return ServiceClientBase.doGetInstance(clientClass, key, paramTypes, paramValues);
    }

    @SuppressWarnings("unchecked")
    private static <DerivedClient extends ServiceClientBase<?>> DerivedClient doGetInstance(Class<DerivedClient> clientClass,
                                                                                            String clientKey,
                                                                                            Class<?>[] paramTypes,
                                                                                            Object[] paramValues) {
        DerivedClient client = (DerivedClient) _clientCache.get(clientKey);
        if (client == null) {
            synchronized (_clientCache) {
                client = (DerivedClient) _clientCache.get(clientKey);
                if (client == null) {
                    try {
                        Constructor<DerivedClient> ctor = clientClass.getDeclaredConstructor(paramTypes);
                        ctor.setAccessible(true);
                        client = ctor.newInstance(paramValues);
                    } catch (Exception e) {
                        throw new RuntimeException("Error occurs when creating client instance.", e);
                    }
                    _clientCache.put(clientKey, client);
                }
            }
        }
        return client;
    }

    public <TReq, TResp> TResp invoke(final String operation, final TReq request, final Class<TResp> responseClass) throws IOException {
        return this.invoke(operation, request, null, responseClass);
    }

    public <TReq, TResp> TResp invoke(final String operation, final TReq request, final Func<TResp> fallbackProvider, final Class<TResp> responseClass) throws IOException {
        try {
            return invoke0(operation, request, fallbackProvider, responseClass);
        } finally {
            cleanThreadLocalSettings();
        }
    }

    public <TReq, TResp> ListenableFuture<TResp> invokeAsync(final String operation, TReq request, final Class<TResp> respClass) throws IOException {
        try {
            return invokeAsync0(operation, request, respClass);
        } finally {
            cleanThreadLocalSettings();
        }
    }

    private <TReq, TResp> TResp invoke0(final String operation, final TReq request, final Func<TResp> fallbackProvider, final Class<TResp> responseClass) throws IOException {
        if (StringUtils.isEmpty(operation)) {
            throw new IllegalArgumentException("Argument 'operation' can not be null or white space");
        }
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("Argument 'request' can not be null");
        }
        ExecutionContext<TResp> context = null;
        try {
            final ExecutionContext<TResp> executionContext = createExecutionContext(operation, request, responseClass, false);
            context = executionContext;
            return invokeInternal(executionContext);
        } catch (Throwable e) {
            log.warn("invoke0_warn", e);
            throw e;
        }
    }

    private <TResp, TReq> ExecutionContext<TResp> createExecutionContext(String operation, TReq request, Class<TResp> responseClass, boolean b) {
        return new ExecutionContext();
    }

    //region SOA-CLIENT core

    /**
     * SOA <--> HTTP 核心实现，使用了Apache httpclient
     */
    private <TReq, TResp> TResp invokeInternal(ExecutionContext<TResp> executionContext) throws IOException {
        CloseableHttpResponse httpResponse = null;
        long serviceCost = 0;
        boolean hasServiceCost = false;
        Stopwatch remoteCallStopwatch = null;
        try {
            // TODO: 2022/3/26 mock return
            if (true) {
                return returnMockResp();
            }
            //请求序列化
            HttpPost httpPost = prepareWebRequest(executionContext);

            //执行HTTP调用 core
            remoteCallStopwatch = Stopwatch.createStarted();
            httpResponse = executeWithRetry(httpPost, executionContext);
            executionContext.setHttpResponse(httpResponse);
            remoteCallStopwatch.stop();

            //负载均衡，服务器健康状态check

            determineContentEncoding(httpResponse);

            //HTTP响应流返回前可以对其做一些过滤逻辑
            applyHttpResponseFilters(executionContext, httpResponse);
            checkHttpResponseStatus(httpResponse);

            //响应反序列化
            TResp response = deserializeResponse(executionContext, httpResponse);
            executionContext.setEndTime(System.currentTimeMillis());
            return response;
        } catch (Throwable e) {
            executionContext.setEndTime(System.currentTimeMillis());
            log.warn("invokeInternal_warn", e);
            throw e;
        } finally {
            closeStream(httpResponse);
            silentClose(httpResponse);
        }
    }

    private <TResp> TResp returnMockResp() {
        HelloResponseType helloResponseType = new HelloResponseType();
        helloResponseType.setCode(200);
        helloResponseType.setMsg("this is result form ruoan soa");
        return (TResp) helloResponseType;
    }

    private <TResp> HttpPost prepareWebRequest(ExecutionContext<TResp> executionContext) throws IOException {
        String baseUri = executionContext.getUrl();
        if (baseUri != null) {
            baseUri = baseUri.endsWith("/") ? baseUri : baseUri + "/";
        }

        String requestUri = baseUri + executionContext.getFormat() + "/" + executionContext.getOperationName();
        HttpPost httpPost = new HttpPost(requestUri);
        executionContext.setHttpRequest(httpPost);
        RequestConfig config = createRequestConfig(executionContext);
        httpPost.setConfig(config);

        for (Map.Entry<String, String> header : _headers.entrySet()) {
            httpPost.addHeader(header.getKey(), header.getValue());
        }
        if (null != executionContext.getEphemeralHeaders()) {
            for (Map.Entry<String, String> ephemeralHeader : executionContext.getEphemeralHeaders().entrySet()) {
                httpPost.setHeader(ephemeralHeader.getKey(), ephemeralHeader.getValue());
            }
        }
        ContentFormatter contentFormatter = executionContext.getContentFormatter();
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, contentFormatter.getContentType());

        // 传递当前服务的caller,当然这里只是一个demo,实际可以在这里添加更多的header
        String caller = executionContext.getCaller();
        if (StringUtils.isNotEmpty(caller)) {
            httpPost.addHeader("x-soa-caller", caller);
        }
        // pass this three timeout setting to service side
        httpPost.addHeader("x-soa-client-connect-timeout", Long.toString(getConnectTimeout(executionContext.getOperationName())));
        httpPost.addHeader("x-soa-client-connection-request-timeout", Long.toString(getRequestTimeout(executionContext.getOperationName())));
        httpPost.addHeader("x-soa-client-socket-timeout", Long.toString(getSocketTimeout(executionContext.getOperationName())));

        // 请求的序列化
        serializeRequest(executionContext, httpPost);

        applyHttpRequestFilters(executionContext, httpPost);

        return httpPost;
    }

    private <TResp> RequestConfig createRequestConfig(ExecutionContext<TResp> executionContext) {
        // 通过配置获取几个超时时间
        int connectionRequestTimeout = 5000;
        int connectTimeout = 1200;
        int socketTimeout = 5000;
        boolean compressionEnabled = true;

        HttpHost proxy = null;
        if (executionContext.isProxyRequest()) {
            proxy = HttpHost.create(executionContext.getProxyHost());
        }

        return RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setContentCompressionEnabled(compressionEnabled)
                .setProxy(proxy)
                .build();
    }

    private long getSocketTimeout(String operationName) {
        return 0;
    }

    private long getRequestTimeout(String operationName) {
        return 0;
    }

    private long getConnectTimeout(String operationName) {
        return 0;
    }


    private <TResp> CloseableHttpResponse executeWithRetry(HttpPost httpPost, ExecutionContext<TResp> executionContext) throws IOException {
        int executionCount = 0;
        IOException retryException = null;
        while (canRetry(executionCount)) {
            try {
                return _client.execute(httpPost);
            } catch (IOException ignored) {
            }
        }
        // build exception and return
        throw retryException;
    }

    private boolean canRetry(int executionCount) {
        // first invoke
        if (executionCount == 0) {
            return true;
        }
        return executionCount <= 5;
    }

    private void determineContentEncoding(HttpResponse httpResponse) {
        Header contentEncodingHeader = httpResponse.getEntity().getContentEncoding();
        if (contentEncodingHeader == null) {
            return;
        }
        HeaderElement[] contentEncodingElements = contentEncodingHeader.getElements();
        for (HeaderElement contentEncodingElement : contentEncodingElements) {
            String contentEncoding = contentEncodingElement.getName();
            if (StringUtils.isEmpty(contentEncoding)) {
                continue;
            }
            if ("gzip".equalsIgnoreCase(contentEncoding)) {
                httpResponse.setEntity(new GzipDecompressingEntity(httpResponse.getEntity()));
                httpResponse.removeHeader(contentEncodingHeader);
                break;
            } else if ("deflate".equalsIgnoreCase(contentEncoding)) {
                httpResponse.setEntity(new DeflateDecompressingEntity(httpResponse.getEntity()));
                httpResponse.removeHeader(contentEncodingHeader);
                break;
            }
        }
    }

    /**
     * 在发起正式请求前可以对序列化后的报文做一些过滤逻辑
     */
    private <TResp> void applyHttpRequestFilters(ExecutionContext<TResp> context, HttpRequestBase request) {
        HttpRequestFilter filter = context.getEphemeralHttpRequestFilter();
        if (filter == null && _localHttpRequestFilter == null && _globalHttpRequestFilter == null) {
            return;
        }

        try {
            if (filter != null) {
                filter.apply(request);
            }

            filter = _localHttpRequestFilter;
            if (filter != null) {
                filter.apply(request);
            }

            filter = _globalHttpRequestFilter;
            if (filter != null) {
                filter.apply(request);
            }
        } catch (Throwable t) {
            log.warn("applyHttpRequestFilters_warn", t);
            throw t;
        }
    }

    /**
     * 在将响应流返回前可以对其做一些过滤逻辑
     */
    private <TResp> void applyHttpResponseFilters(ExecutionContext<TResp> context, HttpResponse response) {
        if (context.getEphemeralHttpResponseFilter() == null && _localHttpResponseFilter == null && _globalHttpResponseFilter == null) {
            return;
        }
        try {
            HttpResponseFilter filter = _globalHttpResponseFilter;
            if (filter != null) {
                filter.apply(response);
            }

            filter = _localHttpResponseFilter;
            if (filter != null) {
                filter.apply(response);
            }

            filter = context.getEphemeralHttpResponseFilter();
            if (filter != null) {
                filter.apply(response);
            }
        } catch (Throwable t) {
            log.warn("applyHttpResponseFilters_warn", t);
            throw t;
        }
    }

    private <TResp> void serializeRequest(ExecutionContext<TResp> executionContext, HttpPost httpPost) throws IOException {
        ContentFormatter contentFormatter = executionContext.getContentFormatter();
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            contentFormatter.serialize(output, executionContext.getRequest());
            byte[] rawContent = output.toByteArray();
            byte[] content = rawContent;
            for (MessageInspector requestMessageInspector : requestMessageInspectors) {
                content = requestMessageInspector.execute(executionContext, content);
            }
            if (content == null) {
                content = rawContent;
            }
            HttpEntity entity = new ByteArrayEntity(content);
            httpPost.setEntity(entity);
        } catch (Exception e) {
            log.warn("serializeRequest_warn", e);
            throw e;
        }
    }

    private <TResp> TResp deserializeResponse(ExecutionContext<TResp> executionContext, HttpResponse httpResponse) throws IOException {
        ContentFormatter formatter = executionContext.getContentFormatter();
        Class<TResp> clazz = executionContext.getResponseClass();

        InputStream inputStream = getResponseStream(httpResponse, executionContext);

        try {
            TResp response = formatter.deserialize(clazz, inputStream);
            executionContext.setResponse(response);
            return response;
        } catch (Exception e) {
            log.warn("deserializeResponse_warn", e);
            throw e;
        }
    }

    private <TResp> InputStream getResponseStream(HttpResponse httpResponse, ExecutionContext<TResp> executionContext) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        if (!_deserializeUseMemoryStream && responseMessageInspectors.isEmpty()) {
            return inputStream;
        }

        try {
            byte[] rawContent = IOUtils.toByteArray(inputStream);
            byte[] content = rawContent;
            for (MessageInspector responseMessageInspector : responseMessageInspectors) {
                content = responseMessageInspector.execute(executionContext, content);
            }
            if (content == null) {
                content = rawContent;
            }
            inputStream = new ByteArrayInputStream(content);
            return inputStream;
        } catch (Exception e) {
            log.warn("getResponseStream_warn", e);
            throw e;
        }
    }

    private void checkHttpResponseStatus(HttpResponse response) {
        if (response.getStatusLine().getStatusCode() <= 200) {
            return;
        }

        String responseContent = getResponseContent(response);
        StatusLine status = response.getStatusLine();
        int statusCode = status.getStatusCode();
        String reasonPhrase = status.getReasonPhrase();

        //do some wrap, and then return
        throw new RuntimeException();
    }

    private String getResponseContent(HttpResponse response) {
        String responseContent = null;
        InputStreamReader reader = null;
        try {
            char[] buffer = new char[512];
            StringBuilder builder = new StringBuilder();
            reader = new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8);
            int length;
            while ((length = reader.read(buffer, 0, buffer.length)) >= 0) {
                builder.append(buffer, 0, length);
            }
            responseContent = builder.toString();
        } catch (IOException ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
            try {
                if (response instanceof CloseableHttpResponse) {
                    ((CloseableHttpResponse) response).close();
                }
            } catch (IOException ignored) {
            }
        }
        return responseContent;
    }

    private void closeStream(HttpResponse response) {
        if (response == null) {
            return;
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            EntityUtils.consumeQuietly(entity);
        }
    }

    private void silentClose(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            final String msg = "Close http response exception";
            log.warn(msg);
        }
    }

    public void setRequestTimeout(int requestTimeout) {

    }

    public void setFormat(String format) {
    }

    public void setConnectTimeout(int connectTimeout) {
    }

    public void setMaxConnectionPerRoute(int maxConnectionPerRoute) {
    }

    public void setIdleTime(int idleTime) {
    }

    public void setSocketTimeout(int socketTimeout) {
    }

    public void setLogCServiceExceptionAsError(boolean b) {
    }

    public void setLogWebExceptionAsError(boolean b) {
    }

    public void setRequestTimeout(String operation, int requestTimeout) {

    }

    public void setSocketTimeout(String operation, int socketTimeout) {

    }

    public void setConnectTimeout(String operation, int connectTimeout) {
    }

    private class ServiceCostInfo {
        private long serviceCost;
        private boolean hasServiceCost;

        public ServiceCostInfo(Header... headers) {
            if (headers != null && headers.length > 0 && StringUtils.isNotEmpty(headers[0].getValue())) {
                try {
                    serviceCost = Long.parseLong(headers[0].getValue());
                    hasServiceCost = true;
                } catch (NumberFormatException e) {
                    log.warn("ServiceCostInfo_warn", e);
                }
            }
        }

        public long getServiceCost() {
            return serviceCost;
        }

        public boolean isHasServiceCost() {
            return hasServiceCost;
        }
    }

    //endregion

    private <TResp, TReq> ListenableFuture<TResp> invokeAsync0(String operation, TReq request, Class<TResp> respClass) {
        return null;
    }

    private void cleanThreadLocalSettings() {
    }

}
