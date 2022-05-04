package com.ustc.ruoan.framework.web.service.inter;

/**
 * @author ruoan
 * @date 2022/5/4 4:25 下午
 */
public interface LockLongPollingService {

    /**
     * 长轮询，拉
     *
     * @return 读取到的结果
     */
    String pull();

    /**
     * 长轮询，推
     *
     * @param data 写入的数据
     * @return 写入的结果
     */
    String push(String data);
}
