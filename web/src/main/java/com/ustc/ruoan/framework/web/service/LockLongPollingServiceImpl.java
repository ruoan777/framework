package com.ustc.ruoan.framework.web.service;

import com.ustc.ruoan.framework.web.longpolling.LockPullTask;
import com.ustc.ruoan.framework.web.longpolling.LockPushTask;
import com.ustc.ruoan.framework.web.service.inter.LockLongPollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author ruoan
 * @date 2022/5/4 4:20 下午
 */
@Service
public class LockLongPollingServiceImpl implements LockLongPollingService {

    @Autowired
    private ScheduledExecutorService scheduler;

    private LockPullTask lockPullTask;

    private Object lock;

    @PostConstruct
    public void post() {
        lock = new Object();
    }

    /**
     * 本地模拟，为方便debug，把这个时间设置的很长
     */
    private final Long TIME_OUT_MILLIS = 1000000L;


    @Override
    public String pull() {
        lockPullTask = new LockPullTask(lock);
        Future<String> future = scheduler.submit(lockPullTask);
        try {
            return future.get(TIME_OUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            return dealTimeOut();
        }
        return "ex";
    }

    private String dealTimeOut() {
        synchronized (lock) {
            lock.notifyAll();
            lockPullTask.setData("timeout");
        }
        return "timeout";
    }

    @Override
    public String push(String data) {
        Future<String> future = scheduler.schedule(new LockPushTask(lockPullTask, data, lock), 0L,
                TimeUnit.MILLISECONDS);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "ex";
    }
}