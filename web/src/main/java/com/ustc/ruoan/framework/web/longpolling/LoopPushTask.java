package com.ustc.ruoan.framework.web.longpolling;

import java.util.concurrent.Callable;

/**
 * @author ruoan
 * @date 2022/5/4 3:58 下午
 */
public class LoopPushTask implements Callable<String> {

    private final LoopPullTask loopPullTask;

    private final String data;

    public LoopPushTask(LoopPullTask loopPullTask, String data) {
        this.loopPullTask = loopPullTask;
        this.data = data;
    }

    @Override
    public String call() throws Exception {
        loopPullTask.setData(data);
        return "changed";
    }
}
