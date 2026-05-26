package com.jnh.framework.ioc;

import com.jnh.framework.ioc.util.ClsUtil;
import com.jnh.framework.ioc.util.Ut;
import lombok.Getter;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 빈 생성을 위한 메타정보(설계도)를 담는 클래스.
 * 실제 빈 객체는 만들지 않고, "어떻게 만들지"만 보관한다.
 *
 * 지원하는 빈 생성 방식:
 *   1. 클래스 기반  - 클래스의 생성자 호출 (@Component 스타일)
 *   2. 메서드 기반  - 설정 클래스의 @Bean 메서드 호출
 */
public class BeanDefinition<T> {
    private final Class<T> cls;
    /**
     * 빈을 만들 때 호출할 대상.
     * Constructor 또는 Method가 들어오므로, 공통 부모인 Executable로 받는다.
     */
    private final Executable makeMethod;
    /** 컨테이너에 등록될 빈 이름 */
    @Getter
    private final String beanName;

    public BeanDefinition(Class<T> cls)  {
        this.cls = cls;
        this.makeMethod = ClsUtil.getConstructor(cls);
        this.beanName = Ut.str.lcfirst(cls.getSimpleName());
    }

    /**
     * [생성자 2] @Bean 메서드 기반
     * 설정 클래스 안의 메서드를 호출해서 빈을 만드는 경우.
     * 메서드명이 곧 빈 이름, 메서드의 반환 타입이 빈 타입이 된다.
     *
     * 예) new BeanDefinition<>(TestJacksonConfig.class, "testBaseJavaTimeModule")
     *     -> JavaTimeModule 타입 빈을 "testBaseJavaTimeModule" 이름으로 등록
     *
     * 메서드를 못 찾으면 NoSuchElementException 발생.
     */
    public BeanDefinition(Class<?> configClass, String beanName)  {
        Method method = Arrays.stream(configClass
                        .getMethods())
                .filter(m -> m.getName().equals(beanName))
                .findFirst()
                .get();

        this.cls = (Class<T>) method.getReturnType();
        this.makeMethod = method;
        this.beanName = beanName;
    }

    /**
     * 이 빈을 만들 때 주입받아야 할 의존성들의 이름 목록.
     * "파라미터 이름 == 주입받을 빈 이름" 규칙을 따른다.
     *
     * 생성 방식에 따라 참조 대상이 달라진다:
     *   - 클래스 기반   : cls의 생성자 파라미터를 본다
     *   - @Bean 메서드 : makeMethod(메서드 자체)의 파라미터를 본다
     *
     * (※ 파라미터 실제 이름을 얻으려면 -parameters 컴파일 옵션 필요)
     */
    public String[] getParameterNames() {
        if (!isCreateTypeMethod()) return ClsUtil.getParameterNames(cls);

        return ClsUtil.getParameterNames(makeMethod);
    }

    /** 이 빈이 의존하는 다른 빈들의 이름 목록 (파라미터명 = 빈 이름 규칙) */
    public String[] getBeanNames() {
        return ClsUtil.getParameterNames(cls);
    }

    /**
     * 빈 생성 방식 판별.
     *   - true  : @Bean 메서드 방식 (Method 호출 필요)
     *   - false : 클래스 생성자 방식 (Constructor 호출 필요)
     *
     * 실제 빈 생성 단계에서 호출 방식을 분기할 때 사용한다.
     */
    public boolean isCreateTypeMethod() {
        return makeMethod instanceof Method;
    }
}