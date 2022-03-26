package com.ustc.ruoan.framework.web.proxy;

import com.ustc.ruoan.framework.soaclient.soa.hello.HelloRequestType;
import com.ustc.ruoan.framework.soaclient.soa.hello.HelloResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ruoan
 * @date 2022/3/26 4:57 下午
 */
@Component
public class Proxy {

    @Autowired
    private HelloProxy helloProxy;

    public HelloResponseType execute(HelloRequestType request) {
        return helloProxy.hello(request);
    }
}
