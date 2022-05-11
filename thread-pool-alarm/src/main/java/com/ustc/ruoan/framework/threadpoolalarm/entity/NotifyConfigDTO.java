package com.ustc.ruoan.framework.threadpoolalarm.entity;

import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyPlatformEnum;
import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyTypeEnum;
import lombok.Data;

/**
 * @author ruoan
 * @date 2022/5/8 7:28 下午
 */
@Data
public class NotifyConfigDTO {

    /**
     * 项目id
     */
    private String appid;

    /**
     * 线程池id
     */
    private String tpId;

    /**
     * 通知平台
     */
    private NotifyPlatformEnum platform;

    /**
     * 通知类型
     */
    private String type;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 加签
     */
    private String secret;

    /**
     * 报警间隔
     */
    private Integer interval;

    /**
     * 接收者
     */
    private String receives;

    /**
     * 报警类型
     */
    private NotifyTypeEnum typeEnum;
}
