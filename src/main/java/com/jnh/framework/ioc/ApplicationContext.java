package com.jnh.framework.ioc;

import com.jnh.domain.testPost.testPost.repository.TestPostRepository;
import com.jnh.domain.testPost.testPost.service.TestFacadePostService;
import com.jnh.domain.testPost.testPost.service.TestPostService;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private Map<String, Object> beans;
    private String basePakage;

    public ApplicationContext(String basePakage){
        this.basePakage = basePakage;
        beans = new HashMap<>();
    }

    public void init() {
    }

    public <T> T getBean(String beanName) {
        Object bean = beans.get(beanName);

        if(bean == null){
            bean = switch (beanName){
                case "testFacadePostService" -> new TestFacadePostService(
                        getBean("testPostService"),
                        getBean("testPostRepository")
                );
                case "testPostService" -> (T) new TestPostService(getBean("testPostRepository"));
                case "testPostRepository" -> (T) new TestPostRepository();
                default -> null;
            };
            beans.put(beanName, bean);
        }

        return (T) bean;
    }
}
