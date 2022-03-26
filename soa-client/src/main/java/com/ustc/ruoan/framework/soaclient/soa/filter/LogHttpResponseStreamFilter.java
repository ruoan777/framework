package com.ustc.ruoan.framework.soaclient.soa.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

/**
 * @author ruoan
 * @date 2022/3/23 11:54 下午
 */
@Slf4j
public class LogHttpResponseStreamFilter implements HttpResponseFilter {

    private static final String DEFAULT_CONTENT_APPLICATION_TYPE = "application/json";

    @Override
    public void apply(HttpResponse response) {
        if (null == response) {
            return;
        }

        try {
            byte[] content = IOUtils.toByteArray(response.getEntity().getContent());
            logBytes(getContentType(response), content);
            response.setEntity(new ByteArrayEntity(content));
        } catch (Exception ex) {
            log.warn("LogHttpResponseStreamError", ex);
        }
    }

    private String getContentType(HttpResponse response) {
        ContentType contentType = ContentType.get(response.getEntity());
        if (null != contentType) {
            String applicationType = contentType.getMimeType();
            if (StringUtils.isNotEmpty(applicationType)) {
                return applicationType;
            }
        }

        return DEFAULT_CONTENT_APPLICATION_TYPE;
    }

    private void logBytes(String contentType, byte[] bytes) {
        if (contentType.contains("protobuf")) {
            StringBuilder sb = new StringBuilder("Byte String (Protobuf Hex):\n");
            for (byte b : bytes) {
                sb.append(String.format("%8s", Integer.toHexString(b)).substring(6, 8).replaceAll(" ", "0")).append(" ");
            }
            log.info("LogHttpResponseStreamSuccess", sb);
        } else {
            String msg = String.format("Byte String:%n%s", new String(bytes));
            log.info("LogHttpResponseStreamSuccess", msg);
        }
    }
}
