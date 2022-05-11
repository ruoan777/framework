package com.ustc.ruoan.framework.threadpoolalarm.entity;

import com.ustc.ruoan.framework.threadpoolalarm.enums.NotifyTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ruoan
 * @date 2022/5/8 7:26 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlarmNotifyRequest extends BaseNotifyRequest{

    /**
     * interval
     */
    private Integer interval;

    /**
     * notifyTypeEnum
     */
    private NotifyTypeEnum notifyTypeEnum;

    /**
     * active
     */
    private String active;

    /**
     * appName
     */
    private String appName;

    /**
     * identify
     */
    private String identify;

    /**
     * corePoolSize
     */
    private Integer corePoolSize;

    /**
     * maximumPoolSize
     */
    private Integer maximumPoolSize;

    /**
     * poolSize
     */
    private Integer poolSize;

    /**
     * activeCount
     */
    private Integer activeCount;

    /**
     * largestPoolSize
     */
    private Integer largestPoolSize;

    /**
     * completedTaskCount
     */
    private Long completedTaskCount;

    /**
     * queueName
     */
    private String queueName;

    /**
     * capacity
     */
    private Integer capacity;

    /**
     * queueSize
     */
    private Integer queueSize;

    /**
     * remainingCapacity
     */
    private Integer remainingCapacity;

    /**
     * executeTime
     */
    private Long executeTime;

    /**
     * executeTimeOut
     */
    private Long executeTimeOut;

    /**
     * executeTimeoutTrace
     */
    private String executeTimeoutTrace;
}
