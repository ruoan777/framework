package com.ustc.ruoan.framework.soaclient.soa.hello;

/**
 * @author ruoan
 * @date 2022/3/21 12:11 上午
 */
public interface Func<V> {
    /**
     * 执行器
     *
     * @return result
     */
    V execute();
}
