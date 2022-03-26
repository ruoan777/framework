package com.ustc.ruoan.framework.soaclient.scan;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author ruoan
 * @date 2022/3/20 3:55 下午
 */
public class RuoanScanner {

    private String basePackage;

    public RuoanScanner(String basePackage) {
        this.basePackage = basePackage;
    }

    @SuppressWarnings("unchecked")
    public Set<Class<?>> scan(Class superClass) {
        Reflections reflections = new Reflections(this.basePackage);
        return reflections.getSubTypesOf(superClass);
    }

    public Set<Class<?>> scanWithAnnotated(final Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(this.basePackage);
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
