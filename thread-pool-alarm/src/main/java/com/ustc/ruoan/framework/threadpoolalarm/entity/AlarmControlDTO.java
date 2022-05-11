package com.ustc.ruoan.framework.threadpoolalarm.entity;

import cn.hutool.core.util.StrUtil;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyPlatformEnum;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @author ruoan
 * @date 2022/5/11 11:27 下午
 */
@Data
@Builder
public class AlarmControlDTO {

    /**
     * 线程池 Id
     */
    private String threadPool;

    /**
     * 推送报警平台
     */
    private NotifyPlatformEnum platform;

    /**
     * 推送报警类型
     */
    private NotifyTypeEnum typeEnum;

    /**
     * 构建线程池报警标识
     */
    public String buildPk() {
        return StrUtil.builder(threadPool, "+", platform.toString()).toString();
    }

}