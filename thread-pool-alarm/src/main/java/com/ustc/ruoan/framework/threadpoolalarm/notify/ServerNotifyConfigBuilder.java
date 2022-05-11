package com.ustc.ruoan.framework.threadpoolalarm.notify;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.ustc.ruoan.framework.threadpoolalarm.config.MockConfigCenter;
import com.ustc.ruoan.framework.threadpoolalarm.entity.NotifyConfigDTO;
import com.ustc.ruoan.framework.threadpoolalarm.entity.ThreadPoolNotifyDTO;
import com.ustc.ruoan.framework.threadpoolalarm.manage.GlobalThreadPoolManage;
import com.ustc.ruoan.framework.threadpoolalarm.notify.inter.NotifyConfigBuilder;
import com.ustc.ruoan.framework.threadpoolalarm.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author ruoan
 * @date 2022/5/12 12:07 上午
 */
@Slf4j
@RequiredArgsConstructor
public class ServerNotifyConfigBuilder implements NotifyConfigBuilder {

    private final AlarmControlHandler alarmControlHandler;

    private final MockConfigCenter configCenter;

    @Override
    public Map<String, List<NotifyConfigDTO>> buildNotify() {
        Map<String, List<NotifyConfigDTO>> resultMap = Maps.newHashMap();
        List<String> threadPoolIds = GlobalThreadPoolManage.listThreadPoolId();
        if (CollUtil.isEmpty(threadPoolIds)) {
            log.warn("The client does not have a dynamic thread pool instance configured.");
            return resultMap;
        }
        //从配置中心获取告警配置
        String resultDataStr = doGetConfigFromConfigCenter(configCenter);

        List<ThreadPoolNotifyDTO> resultData = JsonUtil.parseArray(resultDataStr, ThreadPoolNotifyDTO.class);
        resultData.forEach(each -> resultMap.put(each.getNotifyKey(), each.getNotifyList()));

        resultMap.forEach((key, val) ->
                val.stream().filter(each -> StrUtil.equals("ALARM", each.getType()))
                        .forEach(each -> alarmControlHandler.initCacheAndLock(each.getTpId(), each.getPlatform(), each.getInterval()))
        );

        return resultMap;
    }

    /**
     * 读取公司使用的具体配置中间件
     */
    private String doGetConfigFromConfigCenter(MockConfigCenter configCenter) {
        return "mock config center";
    }
}
