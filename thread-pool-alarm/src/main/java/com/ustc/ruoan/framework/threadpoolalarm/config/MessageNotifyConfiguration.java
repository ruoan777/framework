package com.ustc.ruoan.framework.threadpoolalarm.config;

import com.ustc.ruoan.framework.threadpoolalarm.executor.ThreadPoolNotifyAlarmHandler;
import com.ustc.ruoan.framework.threadpoolalarm.notify.*;
import com.ustc.ruoan.framework.threadpoolalarm.notify.inter.MessageService;
import com.ustc.ruoan.framework.threadpoolalarm.notify.inter.NotifyConfigBuilder;
import com.ustc.ruoan.framework.threadpoolalarm.notify.inter.SendMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ruoan
 * @date 2022/5/8 6:48 下午
 */
@Configuration
public class MessageNotifyConfiguration {

    @Bean
    public MockConfigCenter configCenter() {
        return new MockConfigCenter();
    }

    /**
     * 控制上报频率
     */
    @Bean
    public AlarmControlHandler alarmControlHandler() {
        return new AlarmControlHandler();
    }

    @Bean
    public NotifyConfigBuilder notifyConfigBuilder(AlarmControlHandler alarmControlHandler, MockConfigCenter configCenter) {
        return new ServerNotifyConfigBuilder(alarmControlHandler, configCenter);
    }

    @Bean
    public MessageService hippoSendMessageService(NotifyConfigBuilder notifyConfigBuilder, AlarmControlHandler alarmControlHandler) {
        return new BaseMessageServiceImpl(notifyConfigBuilder, alarmControlHandler);
    }

    /**
     * 定时检测线程池指标并上报，功能相对独立
     */
    @Bean
    public ThreadPoolNotifyAlarmHandler threadPoolNotifyAlarmHandler(MessageService hippoSendMessageService) {
        return new ThreadPoolNotifyAlarmHandler(hippoSendMessageService);
    }


    @Bean
    public SendMessageHandler larkSendMessageHandler() {
        return new EmailSendMessageHandler();
    }

    @Bean
    public SendMessageHandler weChatSendMessageHandler() {
        return new WeChatSendMessageHandler();
    }

}
