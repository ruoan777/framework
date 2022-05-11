package com.ustc.ruoan.framework.threadpoolalarm.executor;

import com.ustc.ruoan.framework.threadpoolalarm.entity.AlarmNotifyRequest;
import com.ustc.ruoan.framework.threadpoolalarm.entity.ThreadPoolNotifyAlarm;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyTypeEnum;
import com.ustc.ruoan.framework.threadpoolalarm.manage.GlobalNotifyAlarmManage;
import com.ustc.ruoan.framework.threadpoolalarm.manage.GlobalThreadPoolManage;
import com.ustc.ruoan.framework.threadpoolalarm.notify.inter.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author ruoan
 * @date 2022/5/8 11:56 下午
 */
@RequiredArgsConstructor
public class ThreadPoolNotifyAlarmHandler implements Runnable, CommandLineRunner {

    private final MessageService messageService;

    @Value("${ruoan.framework.thread.check-state-interval:5}")
    private Integer checkStateInterval;

    private final ScheduledExecutorService ALARM_NOTIFY_EXECUTOR = new ScheduledThreadPoolExecutor(
            1,
            r -> new Thread(r, "client.alarm.notify")
    );

    @Override
    public void run() {
        List<String> listThreadPoolId = GlobalThreadPoolManage.listThreadPoolId();
        listThreadPoolId.forEach(threadPoolId -> {
            ThreadPoolExecutor executor = GlobalThreadPoolManage.getExecutorService(threadPoolId);
            checkPoolCapacityAlarm(threadPoolId, executor);
            checkPoolActivityAlarm(threadPoolId, executor);
        });
    }

    private void checkPoolCapacityAlarm(String threadPoolId, ThreadPoolExecutor executor) {
        if (messageService == null) {
            return;
        }

        ThreadPoolNotifyAlarm threadPoolNotifyAlarm = GlobalNotifyAlarmManage.get(threadPoolId);
        BlockingQueue<Runnable> blockIngQueue = executor.getQueue();

        int queueSize = blockIngQueue.size();
        int capacity = queueSize + blockIngQueue.remainingCapacity();
        int divide = (int) (Double.parseDouble(queueSize + "") / Double.parseDouble(capacity + "") * 100);
        boolean isSend = threadPoolNotifyAlarm.getIsAlarm() && divide > threadPoolNotifyAlarm.getCapacityAlarm();
        if (isSend) {
            AlarmNotifyRequest alarmNotifyRequest = buildAlarmNotifyReq(executor);
            alarmNotifyRequest.setThreadPoolId(threadPoolId);
            messageService.sendAlarmMessage(NotifyTypeEnum.CAPACITY, alarmNotifyRequest);
        }
    }

    private void checkPoolActivityAlarm(String threadPoolId, ThreadPoolExecutor threadPoolExecutor) {
        int activeCount = threadPoolExecutor.getActiveCount();
        int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
        int divide = (int) (Double.parseDouble(activeCount + "") / Double.parseDouble(maximumPoolSize + "") * 100);

        ThreadPoolNotifyAlarm threadPoolNotifyAlarm = GlobalNotifyAlarmManage.get(threadPoolId);
        boolean isSend = threadPoolNotifyAlarm.getIsAlarm() && divide > threadPoolNotifyAlarm.getActiveAlarm();
        if (isSend) {
            AlarmNotifyRequest alarmNotifyRequest = buildAlarmNotifyReq(threadPoolExecutor);
            alarmNotifyRequest.setThreadPoolId(threadPoolId);
            messageService.sendAlarmMessage(NotifyTypeEnum.ACTIVITY, alarmNotifyRequest);
        }
    }

    private AlarmNotifyRequest buildAlarmNotifyReq(ThreadPoolExecutor threadPoolExecutor) {
        AlarmNotifyRequest request = new AlarmNotifyRequest();

        String appName = "ruoanApplication";
        request.setAppName(appName);

        int corePoolSize = threadPoolExecutor.getCorePoolSize();
        int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
        int poolSize = threadPoolExecutor.getPoolSize();
        int activeCount = threadPoolExecutor.getActiveCount();
        int largestPoolSize = threadPoolExecutor.getLargestPoolSize();
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();

        String env = "prod";
        request.setActive(env);
        request.setIdentify(UUID.randomUUID().toString());
        request.setCorePoolSize(corePoolSize);
        request.setMaximumPoolSize(maximumPoolSize);
        request.setPoolSize(poolSize);
        request.setActiveCount(activeCount);
        request.setLargestPoolSize(largestPoolSize);
        request.setCompletedTaskCount(completedTaskCount);

        BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();
        int queueSize = queue.size();
        String queueType = queue.getClass().getSimpleName();
        int remainingCapacity = queue.remainingCapacity();
        int queueCapacity = queueSize + remainingCapacity;
        request.setQueueName(queueType);
        request.setCapacity(queueCapacity);
        request.setQueueSize(queueSize);
        request.setRemainingCapacity(remainingCapacity);
        return request;
    }

    @Override
    public void run(String... args) throws Exception {
        ALARM_NOTIFY_EXECUTOR.scheduleWithFixedDelay(this, 0, checkStateInterval, TimeUnit.SECONDS);
    }
}
