package com.ustc.ruoan.framework.cache.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author ruoan
 * @date 2022/4/1 11:37 下午
 */
@Slf4j
public class GzipUtil {
    private static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    public static String uncompressToString(String inputString) {
        try {
            byte[] bytes = Base64.getDecoder().decode(inputString);
            return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
        } catch (Exception e) {
            log.error("GzipUtil_uncompressToString_error", e);
        }
        return StringUtils.EMPTY;
    }

    public static String compressToString(String str) {
        try {
            return compressToString(str, GZIP_ENCODE_UTF_8);
        } catch (Exception e) {
            log.error("GzipUtil_compressToString_error", e);
        }
        return StringUtils.EMPTY;
    }

    public static String uncompressToString(byte[] bytes, String encoding) throws Exception {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             GZIPInputStream ungzip = new GZIPInputStream(in)) {
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        }
    }

    public static String compressToString(String str, String encoding) throws IOException {
        if (str == null || str.length() == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(compress(str, encoding));
    }

    public static byte[] compress(String str, String encoding) throws IOException {
        if (str == null || str.length() == 0) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(str.getBytes(encoding));
            gzip.close();
            return out.toByteArray();
        }
    }

}
