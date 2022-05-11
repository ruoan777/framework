package com.ustc.ruoan.framework.threadpoolalarm.notify;

import com.ustc.ruoan.framework.threadpoolalarm.entity.NotifyConfigDTO;
import com.ustc.ruoan.framework.threadpoolalarm.entity.NotifyRequest;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyPlatformEnum;
import com.ustc.ruoan.framework.threadpoolalarm.notify.inter.SendMessageHandler;

/**
 * @author ruoan
 * @date 2022/5/8 7:35 下午
 */
public class WeChatSendMessageHandler implements SendMessageHandler {

    @Override
    public NotifyPlatformEnum getType() {
        return NotifyPlatformEnum.WECHAT;
    }

    @Override
    public void sendAlarmMessage(NotifyConfigDTO notifyConfig, NotifyRequest alarmNotifyRequest) {

    }

    @Override
    public void sendChangeMessage(NotifyConfigDTO notifyConfig, NotifyRequest changeParameterNotifyRequest) {

    }
}
