package com.ustc.ruoan.framework.web.service;

import com.ustc.ruoan.framework.web.longpolling.LoopPullTask;
import com.ustc.ruoan.framework.web.longpolling.LoopPushTask;
import com.ustc.ruoan.framework.web.service.inter.LoopLongPollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 长轮询的简易实现版本
 *
 * @author ruoan
 * @date 2022/5/4 3:53 下午
 */
@Service
public class LoopLongPollingServiceImpl implements LoopLongPollingService {

    @Autowired
    private ScheduledExecutorService scheduler;

    private LoopPullTask loopPullTask;

    @Override
    public String pull() {
        loopPullTask = new LoopPullTask();
        Future<String> result = scheduler.schedule(loopPullTask, 0L, TimeUnit.MILLISECONDS);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "ex";
    }

    @Override
    public String push(String data) {
        Future<String> future = scheduler.schedule(new LoopPushTask(loopPullTask, data), 0L, TimeUnit.MILLISECONDS);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "ex";
    }
}
