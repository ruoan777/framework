package com.ustc.ruoan.framework.soaclient.anno;

import com.ustc.ruoan.framework.soaclient.autoconfigure.ServiceClientAutoConfigureRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ruoan
 * @date 2022/3/27 12:20 上午
 */
@Retention(RetentionPolicy.RUNTIME)
@Import(ServiceClientAutoConfigureRegistrar.class)
public @interface ServiceClientScan {
}
