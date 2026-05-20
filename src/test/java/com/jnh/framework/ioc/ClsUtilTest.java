package com.jnh.framework.ioc;

import com.jnh.domain.testPost.testPost.repository.TestPostRepository;
import com.jnh.domain.testPost.testPost.service.TestFacadePostService;
import com.jnh.domain.testPost.testPost.service.TestPostService;
import com.jnh.framework.annotations.Component;
import com.jnh.framework.ioc.util.ClsUtil;
import com.jnh.framework.ioc.util.Ut;
import com.jnh.framework.ioc.util.sample.TestCar;
import com.jnh.framework.ioc.util.sample.TestShip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ClsUtilTest {
    @Test
    @DisplayName("ClsUtil.loadClass")
    void t1() {
        Class<TestCar> cls = ClsUtil.loadClass("com.jnh.framework.ioc.util.sample.TestCar");

        assertThat(cls).isEqualTo(TestCar.class);
    }

    @Test
    @DisplayName("ClsUtil.construct with clsPath")
    void t2() {
        // new TestCar("BMW", 1234); 와 같은 의미
        TestCar testCar = ClsUtil.construct("com.jnh.framework.ioc.util.sample.TestCar", new Object[]{"BMW", 1234});
        // 위 코드는 아래와 같은 의미이다.
        // new TestCar("BMW", 1234);

        assertThat(testCar.getName()).isEqualTo("BMW");
        assertThat(testCar.getNumber()).isEqualTo(1234);
    }

    @Test
    @DisplayName("ClsUtil.construct with cls")
    void t3() {
        // new TestCar("BMW", 1234); 와 같은 의미
        TestCar testCar = ClsUtil.construct(TestCar.class, new Object[]{"BMW", 1234});

        assertThat(testCar.getName()).isEqualTo("BMW");
        assertThat(testCar.getNumber()).isEqualTo(1234);
    }

    @Test
    @DisplayName("ClsUtil.construct")
    void t4() {
        TestShip testShip = ClsUtil.construct("com.jnh.framework.ioc.util.sample.TestShip", new Object[]{"HMM", 1234});
        // 위 코드는 아래와 같은 의미이다.
        // new TestCar("BMW", 1234);

        assertThat(testShip.getName()).isEqualTo("HMM");
        assertThat(testShip.getNumber()).isEqualTo(1234);
    }

    @Test
    @DisplayName("ClsUtil.getParameters with clsPath, parameterTypes")
    void t5() {
        Parameter[] parameters = ClsUtil.getParameters("com.jnh.framework.ioc.util.sample.TestCar", new Class[]{String.class, int.class});

        assertThat(parameters[0].getType()).isEqualTo(String.class);
        assertThat(parameters[0].getName()).isEqualTo("name");
        }

    @Test
    @DisplayName("ClsUtil.getParameters with cls, parameters")
    void t6() {
        Parameter[] parameters = ClsUtil.getParameters(TestCar.class, new Object[]{"BMW", 1234});

        assertThat(parameters[0].getType()).isEqualTo(String.class);
        assertThat(parameters[0].getName()).isEqualTo("name");

        assertThat(parameters[1].getType()).isEqualTo(int.class);
        assertThat(parameters[1].getName()).isEqualTo("number");
    }

    @Test
    @DisplayName("ClsUtil.getParameters with cls, parameterTypes")
    void t7() {
        Parameter[] parameters = ClsUtil.getParameters(TestCar.class, new Class[]{String.class, int.class});

        assertThat(parameters[0].getType()).isEqualTo(String.class);
        assertThat(parameters[0].getName()).isEqualTo("name");

        assertThat(parameters[1].getType()).isEqualTo(int.class);
        assertThat(parameters[1].getName()).isEqualTo("number");
    }

    @Test
    @DisplayName("ClsUtil.getParameterNames with clsPath, parameters")
    void t8() {
        String[] parameterNames = ClsUtil.getParameterNames("com.ll.framework.ioc.util.sample.TestCar", new Object[]{"BMW", 1234});

        assertThat(parameterNames[0]).isEqualTo("name");
        assertThat(parameterNames[1]).isEqualTo("number");
    }

    @DisplayName("ClsUtil.getParameterNames with clsPath, parameterTypes")
    void t9() {
        String[] parameterNames = ClsUtil.getParameterNames("com.ll.framework.ioc.util.sample.TestCar", new Class[]{String.class, int.class});

        assertThat(parameterNames[0]).isEqualTo("name");
        assertThat(parameterNames[1]).isEqualTo("number");
    }

    @Test
    @DisplayName("ClsUtil.getParameterNames with cls, parameters")
    void t10() {
        String[] parameterNames = ClsUtil.getParameterNames(TestCar.class, new Object[]{"BMW", 1234});

        assertThat(parameterNames[0]).isEqualTo("name");
        assertThat(parameterNames[1]).isEqualTo("number");
    }

    @Test
    @DisplayName("ClsUtil.getParameterNames with cls, parameterTypes")
    void t11() {
        String[] parameterNames = ClsUtil.getParameterNames(TestCar.class, new Class[]{String.class, int.class});

        assertThat(parameterNames[0]).isEqualTo("name");
        assertThat(parameterNames[1]).isEqualTo("number");
    }

    @Test
    @DisplayName("annotatedClasses")
    void t13() {
        Map<String, Class<?>> annotatedClasses = ClsUtil.annotatedClasses("com.jnh", Component.class);

        assertThat(annotatedClasses).containsKeys("testFacadePostService", "testPostService", "testPostRepository");
        assertThat(annotatedClasses.get("testFacadePostService")).isEqualTo(TestFacadePostService.class);
        assertThat(annotatedClasses.get("testPostService")).isEqualTo(TestPostService.class);
        assertThat(annotatedClasses.get("testPostRepository")).isEqualTo(TestPostRepository.class);
    }
}