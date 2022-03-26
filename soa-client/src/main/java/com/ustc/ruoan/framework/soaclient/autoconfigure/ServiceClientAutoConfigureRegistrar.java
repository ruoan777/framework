package com.ustc.ruoan.framework.soaclient.autoconfigure;

import com.ustc.ruoan.framework.soaclient.anno.ServiceClient;
import com.ustc.ruoan.framework.soaclient.ServiceClientFacadeFactoryBean;
import com.ustc.ruoan.framework.soaclient.ServiceClientFactoryBean;
import com.ustc.ruoan.framework.soaclient.scan.RuoanScanner;
import com.ustc.ruoan.framework.soaclient.scan.ScanClassAutoConfigureRegister;
import com.ustc.ruoan.framework.soaclient.soa.ServiceClientBase;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * @author ruoan
 * @date 2022/3/20 12:24 下午
 */
public class ServiceClientAutoConfigureRegistrar extends ScanClassAutoConfigureRegister {

    @Override
    public Set<Class<?>> doScan(RuoanScanner scanner) {
        return scanner.scan(ServiceClientBase.class);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        RuoanScanner scanner = new RuoanScanner(getBasePackage());

        // register Service Client
        Set<Class<?>> classes = scanner.scan(ServiceClientBase.class);
        preHandleServiceClientBaseClasses(classes);
        for (Class<?> clazz : classes) {
            GenericBeanDefinition beanDefinition = createServiceClientBeanDefinition(clazz);
            registry.registerBeanDefinition(clazz.getName(), beanDefinition);
        }

        //register Service Client Facade
        Set<Class<?>> facadeClasses = scanner.scanWithAnnotated(ServiceClient.class);
        for (Class<?> clazz : facadeClasses) {
            GenericBeanDefinition beanDefinition = createServiceClientFacadeBeanDefinition(clazz);
            registry.registerBeanDefinition(clazz.getName(), beanDefinition);
        }
    }

    /**
     * 可以提前做一些过滤逻辑，过滤掉不需要扫描的类
     */
    protected void preHandleServiceClientBaseClasses(Set<Class<?>> classes) {
    }

    private GenericBeanDefinition createServiceClientBeanDefinition(Class<?> beanClass) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ServiceClientFactoryBean.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanClass.getName());
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_NO);
        return beanDefinition;
    }

    private GenericBeanDefinition createServiceClientFacadeBeanDefinition(Class<?> beanClass) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ServiceClientFacadeFactoryBean.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanClass.getName());
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_NO);
        return beanDefinition;
    }
}
