package com.jnh.framework.ioc;

import com.jnh.framework.annotations.Bean;
import com.jnh.framework.annotations.Component;
import com.jnh.framework.annotations.Configuration;
import com.jnh.framework.ioc.util.ClsUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;


/**
 * IoC 컨테이너의 핵심 클래스.
 * 지정한 패키지를 스캔해서 빈을 등록하고, 요청 시 의존성을 주입하며 객체를 생성한다.
 *
 * 동작 흐름:
 *   1. init()     : @Component, @Configuration의 @Bean을 모두 BeanDefinition으로 수집
 *   2. genBean()  : 필요한 빈을 재귀적으로 생성 (의존성 자동 주입)
 */
public class ApplicationContext {

    /** 스캔할 패키지 루트 (예: "com.ll.domain") */
    private String basePackage;

    /** 이미 생성된 빈 객체들의 캐시. key: 빈 이름, value: 빈 인스턴스 */
    private Map<String, Object> beans;

    /** @Component로 발견된 클래스 목록. key: 빈 이름, value: 클래스 타입 */
    private Map<String, Class<?>> beanClasses;

    /** 모든 빈 정의(설계도) 모음. key: 빈 이름, value: BeanDefinition */
    Map<String, BeanDefinition> beanDefinitions;

    public ApplicationContext(String basePackage) {
        this.basePackage = basePackage;
        this.beans = new LinkedHashMap<>();
    }

    /**
     * 빈 정의 수집 단계.
     * 두 종류의 빈을 찾아 BeanDefinition으로 만들어 등록한다:
     *   (a) @Component 붙은 클래스          -> 클래스 기반 BeanDefinition
     *   (b) @Configuration 안의 @Bean 메서드 -> 메서드 기반 BeanDefinition
     *
     * 실제 객체 생성은 하지 않고, "어떤 빈이 있는지" 목록만 만든다.
     */
    public void init() {

        this.beanDefinitions = Stream.concat(
                // (a) @Component -> 클래스 기반 BeanDefinition
                ClsUtil.annotatedClasses(basePackage, Component.class)
                        .values()
                        .stream()
                        .map(BeanDefinition::new),

                // (b) @Configuration 클래스들 -> 그 안의 @Bean 메서드들 -> 메서드 기반 BeanDefinition
                ClsUtil.annotatedClasses(basePackage, Configuration.class)
                        .values()
                        .stream()
                        .flatMap(cls ->
                                Arrays.stream(cls.getMethods())
                                        .filter(m -> m.isAnnotationPresent(Bean.class))
                        )
                        .map(method -> new BeanDefinition(method.getDeclaringClass(), method.getName()))
        ).collect(
                // 빈 이름을 key로 해서 Map으로 수집
                LinkedHashMap::new,
                (map, bd) -> map.put(bd.getBeanName(), bd),
                Map::putAll
        );
    }

    /**
     * 빈을 생성하거나 캐시된 것을 반환한다 (싱글톤 보장).
     *
     * 동작:
     *   1. 이미 만들어진 빈이면 그대로 반환
     *   2. 없으면 -> 의존성 빈들을 재귀적으로 먼저 만들고 -> 생성자 호출로 본인 생성 -> 캐시에 저장
     *
     * 의존성은 생성자 파라미터 이름으로 식별한다.
     * 예) UserService(UserRepository userRepository)
     *     -> "userRepository" 빈을 먼저 만든 뒤 주입
     *
     * 현재 한계:
     *   - beanClasses(@Component)에서만 클래스를 찾으므로 @Bean 메서드 빈은 처리 못 함
     *   - 순환 의존성 감지 없음 (StackOverflowError 가능)
     */
    public <T> T getBean(String beanName) {
        Object bean = beans.get(beanName);

        if (bean == null) {
            BeanDefinition<T> beanDefinition = beanDefinitions.get(beanName);

            if (beanDefinition == null) {
                throw new RuntimeException(beanName + " 이름의 빈을 찾을 수 없습니다");
            }

            bean = beanDefinition.createBean(this);

            beans.put(beanName, bean);
        }
        return (T) bean;
    }
}