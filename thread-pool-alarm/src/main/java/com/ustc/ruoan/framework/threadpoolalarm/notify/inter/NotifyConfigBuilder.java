package com.ustc.ruoan.framework.threadpoolalarm.notify.inter;

import com.ustc.ruoan.framework.threadpoolalarm.entity.NotifyConfigDTO;

import java.util.List;
import java.util.Map;

/**
 * @author ruoan
 * @date 2022/5/8 7:27 下午
 */
public interface NotifyConfigBuilder {

    /**
     * 构建通知实体
     *
     * @return 通知实体
     */
    Map<String, List<NotifyConfigDTO>> buildNotify();
}
