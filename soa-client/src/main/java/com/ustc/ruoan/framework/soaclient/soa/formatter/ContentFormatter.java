package com.ustc.ruoan.framework.soaclient.soa.formatter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ruoan
 * @date 2022/3/24 12:10 上午
 */
public interface ContentFormatter {

    String getContentType();

    String getExtension();

    String getEncoding();

    <T> void serialize(OutputStream outputStream, T obj) throws IOException;

    <T> T deserialize(Class<T> clazz, InputStream inputStream) throws IOException;
}
