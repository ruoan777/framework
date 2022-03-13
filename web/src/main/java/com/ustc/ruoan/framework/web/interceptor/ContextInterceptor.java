package com.ustc.ruoan.framework.web.interceptor;


import com.ustc.ruoan.framework.web.context.PerformanceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author ruoan
 */
@Slf4j
public class ContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        PerformanceContext.create();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //这里拿到请求，就可以做日志记录了
        Map<String, String> performanceMap = PerformanceContext.get();
        log.info("ContextFilter log is {}", performanceMap.toString());
        PerformanceContext.clear();
    }
}
