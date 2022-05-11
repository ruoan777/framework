package com.ustc.ruoan.framework.threadpoolalarm.notify.inter;

import com.ustc.ruoan.framework.threadpoolalarm.entity.AlarmNotifyRequest;
import com.ustc.ruoan.framework.threadpoolalarm.entity.ChangeParameterNotifyRequest;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyTypeEnum;

/**
 * @author ruoan
 * @date 2022/5/8 7:24 下午
 */
public interface MessageService {

    /**
     * Send alarm message.
     *
     * @param typeEnum
     * @param alarmNotifyRequest
     */
    void sendAlarmMessage(NotifyTypeEnum typeEnum, AlarmNotifyRequest alarmNotifyRequest);

    /**
     * Send change message.
     *
     * @param changeParameterNotifyRequest
     */
    void sendChangeMessage(ChangeParameterNotifyRequest changeParameterNotifyRequest);
}
