package com.ustc.ruoan.framework.soaclient.autoconfigure;

import com.ustc.ruoan.framework.soaclient.invoker.ClientInvokerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ruoan
 */
@Configuration
@Import(ServiceClientAutoConfigureRegistrar.class)
public class SoaClientAutoConfiguration {

    @Bean
    public ClientInvokerProxy clientInvokerProxy() {
        return new ClientInvokerProxy();
    }
}
