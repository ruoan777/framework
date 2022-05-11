package com.ustc.ruoan.framework.threadpoolalarm.manage;

import com.ustc.ruoan.framework.threadpoolalarm.entity.ThreadPoolNotifyAlarm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ruoan
 * @date 2022/5/9 12:28 上午
 */
public class GlobalNotifyAlarmManage {

    private static final Map<String, ThreadPoolNotifyAlarm> NOTIFY_ALARM_MAP = new ConcurrentHashMap();

    public static ThreadPoolNotifyAlarm get(String key) {
        return NOTIFY_ALARM_MAP.get(key);
    }

    public static void put(String key, ThreadPoolNotifyAlarm val) {
        NOTIFY_ALARM_MAP.put(key, val);
    }

}
