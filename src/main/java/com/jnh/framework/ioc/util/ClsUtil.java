package com.jnh.framework.ioc.util;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class ClsUtil {
    @SneakyThrows // 예외 처리 자동처리, forName 은 예외 처리 선언을 해야하나 생략가능
    public static <T> Class<T> loadClass(String clsPath) { // <T> 제네릭 타입파라미터 선언 - 메서드 안에서 T 타입 사용 가능
        return (Class<T>) Class.forName(clsPath); // 문자열로 주어진 클래스 이름을 실제 클래스로 로드해 리턴
        // Class<?> 이렇게 하면 매번 캐스팅이 필요하다. 그래서 여기서는 제네릭으로 형변환 해주면 호출하는 쪽에서 안전하게 사용가능
    }

    public static <T> T construct(String clsPath, Object[] args) {
        return construct(loadClass(clsPath), args);
    }

    @SneakyThrows
    public static <T> T construct(Class<T> cls, Object[] args ) {
        Constructor<T> constructor = getConstructor(cls,args);

        return constructor.newInstance(args);
    }

    private static <T> Constructor<T> getConstructor(Class<T> cls, Object[] args) {
        return (Constructor<T>) cls.getDeclaredConstructors()[0];
    }

    public static Parameter[] getParameters(String clsPath, Object[] args) {
        Class<?> cls = loadClass(clsPath);

        Constructor<?> constructor = getConstructor(cls, args);

        return constructor.getParameters();
    }
}
