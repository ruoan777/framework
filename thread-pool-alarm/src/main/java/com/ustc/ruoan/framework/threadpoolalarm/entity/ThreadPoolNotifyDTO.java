package com.ustc.ruoan.framework.threadpoolalarm.entity;

import lombok.Data;

import java.util.List;

/**
 * @author ruoan
 * @date 2022/5/12 12:39 上午
 */
@Data
public class ThreadPoolNotifyDTO {

    /**
     * notifyKey
     */
    private String notifyKey;

    /**
     * notifyList
     */
    private List<NotifyConfigDTO> notifyList;

}
