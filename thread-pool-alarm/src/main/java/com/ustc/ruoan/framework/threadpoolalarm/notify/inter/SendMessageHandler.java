package com.ustc.ruoan.framework.threadpoolalarm.notify.inter;

import com.ustc.ruoan.framework.threadpoolalarm.entity.NotifyConfigDTO;
import com.ustc.ruoan.framework.threadpoolalarm.entity.NotifyRequest;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyPlatformEnum;

/**
 * @author ruoan
 * @date 2022/5/8 7:34 下午
 */
public interface SendMessageHandler<T extends NotifyRequest, R extends NotifyRequest> {

    /**
     * 消息发送平台
     *
     * @return
     */
    NotifyPlatformEnum getType();

    /**
     * 发送告警信息
     *
     * @param notifyConfig
     * @param alarmNotifyRequest
     */
    void sendAlarmMessage(NotifyConfigDTO notifyConfig, T alarmNotifyRequest);

    /**
     * 发送变更消息
     *
     * @param notifyConfig
     * @param changeParameterNotifyRequest
     */
    void sendChangeMessage(NotifyConfigDTO notifyConfig, R changeParameterNotifyRequest);
}
