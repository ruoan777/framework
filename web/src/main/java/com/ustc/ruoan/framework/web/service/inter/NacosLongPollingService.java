package com.ustc.ruoan.framework.web.service.inter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ruoan
 * @date 2022/5/4 4:40 下午
 */
public interface NacosLongPollingService {

    /**
     * 长轮询，拉
     *
     * @param dataId 数据标识
     * @param req    servlet 请求
     * @param resp   servlet 响应
     */
    void doGet(String dataId, HttpServletRequest req, HttpServletResponse resp);

    /**
     * 长轮询，推
     *
     * @param dataId 数据标识
     * @param data   写入的结果
     */
    void push(String dataId, String data);
}