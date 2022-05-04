package com.ustc.ruoan.framework.web.longpolling;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author ruoan
 * @date 2022/5/4 3:57 下午
 */
@Slf4j
public class LoopPullTask implements Callable<String> {

    @Getter
    @Setter
    public volatile String data;

    /**
     * 本地模拟，为方便debug，把这个时间设置的很长
     */
    private final Long TIME_OUT_MILLIS = 10000L;

    @Override
    public String call() throws Exception {
        Long startTime = System.currentTimeMillis();
        while (true) {
            if (StringUtils.isNotEmpty(data)) {
                return data;
            }
            if (isTimeOut(startTime)) {
                log.info("获取数据请求超时" + new Date());
                data = "请求超时";
                return data;
            }
            //减轻CPU压力
            Thread.sleep(200);
        }
    }

    private boolean isTimeOut(Long startTime) {
        Long nowTime = System.currentTimeMillis();
        return nowTime - startTime > TIME_OUT_MILLIS;
    }
}