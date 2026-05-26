package com.jnh.framework.ioc;

import com.jnh.framework.annotations.Component;
import com.jnh.framework.ioc.util.ClsUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private String basePackage;
    private Map<String, Object> beans;
    private Map<String, Class<?>> beanClasses;

    public ApplicationContext(String basePackage) {
        this.basePackage = basePackage;
        this.beans = new HashMap<>();
    }

    public void init() {
        beanClasses = ClsUtil.annotatedClasses(basePackage, Component.class);
    }

    public <T> T getBean(String beanName) {
        Object bean = beans.get(beanName);

        if (bean == null) {
            Class<T> cls = (Class<T>) beanClasses.get(beanName);

            String[] parameterNames = ClsUtil.getParameterNames(cls);

            Object[] args = Arrays.stream(parameterNames)
                    .map(this::getBean)
                    .toArray();

            bean = ClsUtil.construct(cls, args);

            beans.put(beanName, bean);
        }
        return (T) bean;
    }
}