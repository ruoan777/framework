package com.ustc.ruoan.framework.threadpoolalarm.notify;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.ustc.ruoan.framework.threadpoolalarm.config.ApplicationContextHolder;
import com.ustc.ruoan.framework.threadpoolalarm.entity.AlarmControlDTO;
import com.ustc.ruoan.framework.threadpoolalarm.entity.AlarmNotifyRequest;
import com.ustc.ruoan.framework.threadpoolalarm.entity.ChangeParameterNotifyRequest;
import com.ustc.ruoan.framework.threadpoolalarm.entity.NotifyConfigDTO;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyPlatformEnum;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyTypeEnum;
import com.ustc.ruoan.framework.threadpoolalarm.notify.inter.MessageService;
import com.ustc.ruoan.framework.threadpoolalarm.notify.inter.NotifyConfigBuilder;
import com.ustc.ruoan.framework.threadpoolalarm.notify.inter.SendMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.Map;

/**
 * @author ruoan
 * @date 2022/5/8 7:26 下午
 */
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
@Slf4j
public class BaseMessageServiceImpl implements MessageService, CommandLineRunner {

    private final NotifyConfigBuilder notifyConfigBuilder;

    private final AlarmControlHandler alarmControlHandler;

    /**
     * 发通知所使用的配置，在应用启动时加载好
     */
    private final Map<String, List<NotifyConfigDTO>> notifyConfigs = Maps.newHashMap();

    /**
     * 具体实施发通知的处理器，在应用启动时加载好
     */
    private final Map<NotifyPlatformEnum, SendMessageHandler> sendMessageHandlers = Maps.newHashMap();

    @Override
    public void sendAlarmMessage(NotifyTypeEnum typeEnum, AlarmNotifyRequest alarmNotifyRequest) {
        String threadPoolId = alarmNotifyRequest.getThreadPoolId();
        String buildKey = StrUtil.builder(threadPoolId, "+", "ALARM").toString();
        List<NotifyConfigDTO> notifyList = notifyConfigs.get(buildKey);
        if (CollectionUtils.isEmpty(notifyList)) {
            return;
        }

        for (NotifyConfigDTO each : notifyList) {
            try {
                SendMessageHandler messageHandler = sendMessageHandlers.get(each.getPlatform());
                if (messageHandler == null) {
                    log.warn("Please configure alarm notification on the server. key :: [{}]", threadPoolId);
                    continue;
                }

                if (isSendAlarm(each.getTpId(), each.getPlatform(), typeEnum)) {
                    alarmNotifyRequest.setNotifyTypeEnum(typeEnum);
                    messageHandler.sendAlarmMessage(each, alarmNotifyRequest);
                }
            } catch (Exception ex) {
                log.warn("Failed to send thread pool alarm notification. key :: [{}]", threadPoolId, ex);
            }
        }
    }

    private boolean isSendAlarm(String tpId, NotifyPlatformEnum platform, NotifyTypeEnum typeEnum) {
        AlarmControlDTO alarmControl = AlarmControlDTO.builder()
                .threadPool(tpId)
                .platform(platform)
                .typeEnum(typeEnum)
                .build();
        return alarmControlHandler.isSendAlarm(alarmControl);
    }

    @Override
    public void sendChangeMessage(ChangeParameterNotifyRequest changeParameterNotifyRequest) {
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, SendMessageHandler> sendMessageHandlerMap = ApplicationContextHolder.getBeansOfType(SendMessageHandler.class);
        sendMessageHandlerMap.values().forEach(each -> sendMessageHandlers.put(each.getType(), each));
    }
}
