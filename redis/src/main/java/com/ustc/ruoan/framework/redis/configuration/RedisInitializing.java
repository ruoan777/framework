package com.ustc.ruoan.framework.redis.configuration;

import com.ustc.ruoan.framework.redis.ano.RedisProvider;
import com.ustc.ruoan.framework.redis.provider.RedisCacheProvider;
import com.ustc.ruoan.framework.redis.spring.RedisAutoConfigurationBean;
import com.ustc.ruoan.framework.redis.util.AopTargetUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * @author ruoan
 */
@Slf4j
public class RedisInitializing extends RedisAutoConfigurationBean {

    private static final String BASE_PACKAGE = "com.ustc.ruoan";

    public RedisInitializing() {
    }

    @SneakyThrows
    @Override
    public void afterPropertiesSet() {
    }

    @SneakyThrows
    public void init() {
        Reflections scanner = new Reflections(BASE_PACKAGE, new FieldAnnotationsScanner());
        Set<Field> fields = scanner.getFieldsAnnotatedWith(RedisProvider.class);
        for (Field field : fields) {
            Map<String, ?> values = getApplicationContext().getBeansOfType(field.getDeclaringClass());
            if (values.isEmpty()) {
                continue;
            }
            for (Object target : values.values()) {
                RedisProvider provider = field.getAnnotation(RedisProvider.class);
                ReflectionUtils.makeAccessible(field);
                log.info(" set " + target.getClass().getName() + " redis field name : " + provider.value());
                Object singletonTarget = AopProxyUtils.getSingletonTarget(target);
                target = AopTargetUtils.getTarget(target);
                field.set(target, new RedisCacheProvider("REDIS"));
            }
        }
    }
}