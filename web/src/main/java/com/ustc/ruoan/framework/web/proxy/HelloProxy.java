package com.ustc.ruoan.framework.web.proxy;

import com.ustc.ruoan.framework.soaclient.anno.ServiceClient;
import com.ustc.ruoan.framework.soaclient.anno.ServiceClientConfigOptions;
import com.ustc.ruoan.framework.soaclient.soa.hello.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ruoan
 * @date 2022/3/26 4:59 下午
 */
@Component
public class HelloProxy {

    @Autowired
    private HelloProxyDemo adapt;

    public HelloResponseType hello(HelloRequestType request) {
        return adapt.hello(request);
    }

    @ServiceClient(value = HelloClient.class, socketTimeout = 3000, ignoreError = true)
    private interface HelloProxyDemo {

        /**
         * 健康检查
         *
         * @param request CheckHealthRequestType
         * @return CheckHealthResponseType
         */
        CheckHealthResponseType checkHealth(CheckHealthRequestType request);

        /**
         * hello
         *
         * @param request HelloRequestType
         * @return HelloResponseType
         */
        @ServiceClientConfigOptions(socketTimeout = 2000, logEnable = false)
        HelloResponseType hello(HelloRequestType request);
    }
}
