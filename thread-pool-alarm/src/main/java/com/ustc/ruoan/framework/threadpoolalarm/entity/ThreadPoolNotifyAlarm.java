package com.ustc.ruoan.framework.threadpoolalarm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * @author ruoan
 * @date 2022/5/9 12:28 上午
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ThreadPoolNotifyAlarm {

    @NonNull
    private Boolean isAlarm;

    @NonNull
    private Integer activeAlarm;

    @NonNull
    private Integer capacityAlarm;

    private Integer interval;

    private String receive;

    private Map<String, String> receives;
}
