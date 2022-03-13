package com.ustc.ruoan.ruoandebug.aspect;

import com.ustc.ruoan.ruoandebug.anno.RedisProvider;
import com.ustc.ruoan.ruoandebug.redis.CacheProvider;
import com.ustc.ruoan.ruoandebug.spring.SpringInitializingBean;
import com.ustc.ruoan.ruoandebug.util.AopTargetUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;


/**
 * @author ruoan
 */
public class RedisInitializingAspect extends SpringInitializingBean {

    private final Logger LOGGER = LoggerFactory.getLogger(RedisInitializingAspect.class);

    private static final String BASE_PACKAGE = "com.ustc.ruoan";

    @Override
    public void afterPropertiesSet() throws Exception {
        Reflections scanner = new Reflections(BASE_PACKAGE, Scanners.FieldsAnnotated);
        Set<Field> fields = scanner.getFieldsAnnotatedWith(RedisProvider.class);
        for (Field field : fields) {
            Map<String, ?> values = getApplicationContext().getBeansOfType(field.getDeclaringClass());
            if (values.isEmpty()) {
                continue;
            }
            for (Object target : values.values()) {
                RedisProvider provider = field.getAnnotation(RedisProvider.class);
                ReflectionUtils.makeAccessible(field);
                LOGGER.info(" set " + target.getClass().getName() + " redis field name : " + provider.value());
                target = AopTargetUtils.getTarget(target);
                field.set(target, new CacheProvider("REDIS"));
            }
        }
    }
}