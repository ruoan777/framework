package com.ustc.ruoan.framework.web.longpolling;

import java.util.Iterator;
import java.util.Queue;

/**
 * @author ruoan
 * @date 2022/5/4 4:45 下午
 */
public class NacosPushTask implements Runnable {
    private final String dataId;
    private final String data;
    private final Queue<NacosPullTask> nacosPullTasks;

    public NacosPushTask(String dataId, String data, Queue<NacosPullTask> nacosPullTasks) {
        this.dataId = dataId;
        this.data = data;
        this.nacosPullTasks = nacosPullTasks;
    }

    @Override
    public void run() {
        Iterator<NacosPullTask> iterator = nacosPullTasks.iterator();
        while (iterator.hasNext()) {
            NacosPullTask nacosPullTask = iterator.next();
            if (dataId.equals(nacosPullTask.getDataId())) {
                //可根据内容的MD5判断数据是否发生改变,这里为了演示简单就不写了
                //移除队列中的任务,确保下次请求时响应的task不是上次请求留在队列中的task
                iterator.remove();
                //执行数据变更,发送响应
                nacosPullTask.sendResponse(data);
                break;
            }
        }
    }
}