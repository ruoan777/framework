package com.ustc.ruoan.framework.web.longpolling;

import com.ustc.ruoan.framework.web.entity.Result;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ruoan
 * @date 2022/5/4 4:44 下午
 */
@Slf4j
public class NacosPullTask implements Runnable {

    @Getter
    private final String dataId;
    private final Queue<NacosPullTask> nacosPullTasks;
    private final ScheduledExecutorService scheduler;
    private final AsyncContext asyncContext;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private Future<?> asyncTimeoutFuture;

    public NacosPullTask(Queue<NacosPullTask> nacosPullTasks, ScheduledExecutorService scheduler,
                         AsyncContext asyncContext, String dataId, HttpServletRequest req, HttpServletResponse resp) {
        this.nacosPullTasks = nacosPullTasks;
        this.scheduler = scheduler;
        this.asyncContext = asyncContext;
        this.dataId = dataId;
        this.req = req;
        this.resp = resp;
    }

    /**
     * NacosPullTask负责拉取变更内容,注意内部类中的this指向内部类本身,而非引用匿名内部类的对象.
     */
    @Override
    public void run() {
        asyncTimeoutFuture = scheduler.schedule(() -> {
            log.info("10秒后开始执行长轮询任务:" + new Date());
            /*
              第10秒，将任务移除出队列
              注意这里如果remove this会失败。内部类中的this指向的并非当前对象，而是匿名内部类对象
             */
            nacosPullTasks.remove(NacosPullTask.this);
            sendResponse("超时了");
        }, 10, TimeUnit.SECONDS);
        //第0秒将任务add进队列
        nacosPullTasks.add(this);
    }

    /**
     * 发送响应
     */
    public void sendResponse(String result) {
        System.out.println("发送响应:" + new Date());
        //取消等待执行的任务,避免已经响完了,还有资源被占用
        if (asyncTimeoutFuture != null) {
            //设置为true会立即中断执行中的任务,false对执行中的任务无影响,但会取消等待执行的任务
            asyncTimeoutFuture.cancel(false);
        }

        //设置页码编码
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");

        //禁用缓存
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache,no-store");
        resp.setDateHeader("Expires", 0);
        resp.setStatus(HttpServletResponse.SC_OK);
        //输出Json流
        sendJsonResult(result);
    }

    /**
     * 发送响应流
     *
     * @param result
     */
    private void sendJsonResult(String result) {
        Result<String> res = new Result<>();
        res.setCode(200);
        res.setSuccess(StringUtils.isNotEmpty(result));
        res.setData(result);
        PrintWriter writer = null;
        try {
            writer = asyncContext.getResponse().getWriter();
            writer.write(res.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            asyncContext.complete();
            if (null != writer) {
                writer.close();
            }
        }
    }
}