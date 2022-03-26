package com.ustc.ruoan.framework.soaclient.autoconfigure;

import com.ustc.ruoan.framework.soaclient.anno.ServiceClientScan;
import com.ustc.ruoan.framework.soaclient.invoker.ClientInvokerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ruoan
 */
@Configuration
@ServiceClientScan
public class SoaClientAutoConfiguration {

    @Bean
    public ClientInvokerProxy clientInvokerProxy() {
        return new ClientInvokerProxy();
    }
}
