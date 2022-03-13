package com.ustc.ruoan.framework.web.aspect;

import com.ustc.ruoan.framework.web.anno.MethodLog;
import com.ustc.ruoan.framework.web.context.PerformanceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

/**
 * @author ruoan
 */
@Aspect
@Component
@Slf4j
public class MethodLogAspect {

    @Pointcut("@annotation(com.ustc.ruoan.framework.web.anno.MethodLog)")
    public void pointCutMethod() {
    }

    @Around("pointCutMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        MethodLog annotation = method.getAnnotation(MethodLog.class);
        String methodLogTitle = annotation.value();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result;
        try {
            result = pjp.proceed();
        } finally {
            stopWatch.stop();
            addMethodLogInfo(getMethodLogTitle(method, methodLogTitle), stopWatch.getTotalTimeMillis());
        }
        return result;
    }


    private String getMethodLogTitle(Method method, String methodLogTitle) {
        if (StringUtils.isEmpty(methodLogTitle)) {
            methodLogTitle = method.getDeclaringClass().getSimpleName().concat("_").concat(method.getName());
        }
        return methodLogTitle;
    }


    private void addMethodLogInfo(String methodLogTitle, long time) {
        log.info(String.format("MethodLogAspect_addMethodLogInfo: 方法名称:%s 耗时:%s", methodLogTitle, time));
        PerformanceContext.add(methodLogTitle, time);
    }
}
