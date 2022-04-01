package com.ustc.ruoan.framework.cache.generator;

import com.ustc.ruoan.framework.cache.util.JsonUtil;
import com.ustc.ruoan.framework.cache.util.ReflectionUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ruoan
 * @date 2022/4/1 10:00 下午
 */
public class DefaultKeyGenerator extends KeyGenerator {

    public static final Pattern PATTERN = Pattern.compile("\\d+\\.?[\\w]*");

    @Override
    public String buildKey(String key, Class<?>[] parameterTypes, Object[] arguments) {
//        if (key.contains("{")) {
//            Matcher matcher = PATTERN.matcher(key);
//            while (matcher.find()) {
//                String tmp = matcher.group();
//                String[] express = matcher.group().split("\\.");
//                String i = express[0];
//                int index = Integer.parseInt(i) - 1;
//                Object value = arguments[index];
//                if (parameterTypes[index].isAssignableFrom(List.class)) {
//                    List result = (List) arguments[index];
//                    value = String.join(",", result);
//                }
//                if (value == null || "null".equals(value)) {
//                    value = "";
//                }
//                if (express.length > 1) {
//                    String field = express[1];
//                    value = ReflectionUtils.getFieldValue(value, field);
//                }
//                key = key.replace("{" + tmp + "}", value.toString());
//            }
//        } else if (arguments.length == 1 && !"all".equalsIgnoreCase(key)) {
//            Object argument = arguments[0];
//            String data = JsonUtil.toJsonString(argument);
//            key = getHash(data);
//        }
        return key;
    }

    public static String getHash(String data) {
        String md5Hex = DigestUtils.md5Hex(data);
        String sha1Hex = DigestUtils.sha1Hex(data);
        return md5Hex + sha1Hex;
    }
}
