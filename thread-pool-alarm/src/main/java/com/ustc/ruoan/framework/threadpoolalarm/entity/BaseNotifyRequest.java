package com.ustc.ruoan.framework.threadpoolalarm.entity;

import lombok.Data;

/**
 * @author ruoan
 * @date 2022/5/9 12:35 上午
 */
@Data
public class BaseNotifyRequest implements NotifyRequest {

    private String threadPoolId;

}
