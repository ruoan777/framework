package com.ustc.ruoan.ruoandebug.filter;

import com.ustc.ruoan.ruoandebug.context.PerformanceContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Map;

/**
 * @author ruoan
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "context-filter", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD}, asyncSupported = true)
public class ContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("ContextFilter is initialing");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        PerformanceContext.create();
        filterChain.doFilter(servletRequest, servletResponse);
        //这里拿到请求，就可以做日志记录了
        Map<String, String> performanceMap = PerformanceContext.get();
        log.info("ContextFilter log is {}", performanceMap.toString());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
