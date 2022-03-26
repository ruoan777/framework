package com.ustc.ruoan.framework.soaclient.scan;

import lombok.NonNull;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * @author ruoan
 * @date 2022/3/20 12:26 下午
 */
public abstract class ScanClassAutoConfigureRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    private String basePackage;

    /**
     * 供子类实现的扫描方法，由子类决定扫描何种类型的类
     *
     * @param scanner 自定义扫描器
     * @return 扫描到的符合要求的类
     */
    public abstract Set<Class<?>> doScan(RuoanScanner scanner);

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        RuoanScanner scanner = new RuoanScanner(getBasePackage());
        Set<Class<?>> classes = doScan(scanner);
        for (Class<?> clazz : classes) {
            GenericBeanDefinition beanDefinition = createBeanDefinition(clazz);
            registry.registerBeanDefinition(clazz.getName(), beanDefinition);
        }
    }

    protected String getBasePackage() {
        return this.basePackage != null && !this.basePackage.isEmpty() ?
                this.basePackage : this.environment.getProperty("ustc.base-package", "com.ustc.ruoan");
    }

    private GenericBeanDefinition createBeanDefinition(Class beanClass) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_NO);
        return beanDefinition;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
