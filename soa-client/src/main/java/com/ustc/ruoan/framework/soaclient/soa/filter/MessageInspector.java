package com.ustc.ruoan.framework.soaclient.soa.filter;

import com.ustc.ruoan.framework.soaclient.soa.ExecutionContext;

/**
 * @author ruoan
 * @date 2022/3/24 12:06 上午
 */
public interface MessageInspector {

    /**
     * 消息检查器
     *
     * @param executionContext 执行上下文
     * @param rawMessage       原始HTTP响应体
     * @return 二进制字节数组
     */
    byte[] execute(ExecutionContext executionContext, byte[] rawMessage);
}
