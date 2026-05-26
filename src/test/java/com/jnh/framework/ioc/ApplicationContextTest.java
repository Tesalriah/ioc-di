package com.jnh.framework.ioc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jnh.domain.testPost.testPost.repository.TestPostRepository;
import com.jnh.domain.testPost.testPost.service.TestFacadePostService;
import com.jnh.domain.testPost.testPost.service.TestPostService;
import com.jnh.global.TestJacksonConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationContextTest {
    private static ApplicationContext applicationContext;

    @BeforeAll
    public static void beforeAll() {
        applicationContext = new ApplicationContext("com");
        applicationContext.init();
    }

    @Test
    @DisplayName("ApplicationContext 객체 생성")
    public void t1() {
        System.out.println(applicationContext);
    }


    @Test
    @DisplayName("testPostService 빈 얻기")
    public void t2() {
        TestPostService testPostService = applicationContext
                .getBean("testPostService");

        assertThat(testPostService).isNotNull();
    }

    @Test
    @DisplayName("testPostService 빈을 다시 얻기, 싱글톤이어야 함")
    public void t3() {
        TestPostService testPostService1 = applicationContext
                .getBean("testPostService");

        TestPostService testPostService2 = applicationContext
                .getBean("testPostService");

        assertThat(testPostService1).isSameAs(testPostService2);
    }

    @Test
    @DisplayName("testPostRepository")
    public void t4() {
        TestPostRepository testPostRepository = applicationContext
                .getBean("testPostRepository");

        assertThat(testPostRepository).isNotNull();
    }

    @Test
    @DisplayName("testPostService has testPostRepository")
    public void t5() {
        TestPostService testPostService = applicationContext
                .getBean("testPostService");

        // assertThat(객체).hasFieldOrPropertyWithValue("필드명", 기대값)→ 객체 내부의 필드/프로퍼티가 해당 값과 같은지 검증.들어있는지 검증
        assertThat(testPostService).hasFieldOrPropertyWithValue(
                "testPostRepository",
                applicationContext.getBean("testPostRepository")
        );
    }

    @Test
    @DisplayName("testFacadePostService has testPostService, testPostRepository")
    public void t6() {
        TestFacadePostService testFacadePostService = applicationContext
                .getBean("testFacadePostService");

        assertThat(testFacadePostService).hasFieldOrPropertyWithValue(
                "testPostService",
                applicationContext.getBean("testPostService")
        );

        assertThat(testFacadePostService).hasFieldOrPropertyWithValue(
                "testPostRepository",
                applicationContext.getBean("testPostRepository")
        );
    }

    @Test
    @DisplayName("@Bean, 아무런 의존관계가 없는 단순한 빈인 testBaseJavaTimeModule 를 생성")
    public void t7() {
        JavaTimeModule testBaseJavaTimeModule = applicationContext.getBean("testBaseJavaTimeModule");

        assertThat(testBaseJavaTimeModule).isNotNull();
    }

    @Test
    @DisplayName("@Bean, testBaseJavaTimeModule 빈에 의존하는 testBaseObjectMapper 빈을 생성")
    public void t8() {
        ObjectMapper testBaseObjectMapper = applicationContext.getBean("testBaseObjectMapper");

        assertThat(testBaseObjectMapper).isNotNull();
    }

    @Test
    @DisplayName("BeanDefinition 은 Bean의 생성정보를 담고 있습니다.")
    public void t9() {
        BeanDefinition<TestPostService> beanDefinition = new BeanDefinition<>(TestPostService.class);

        assertThat(beanDefinition.getBeanName()).isEqualTo("testPostService");
        assertThat(beanDefinition.isCreateTypeMethod()).isFalse();
    }

    @Test
    @DisplayName("beanDefinition.getParameterNames")
    public void t10() {
        BeanDefinition<TestPostService> beanDefinition = new BeanDefinition<>(TestPostService.class);
        String[] parameterNames = beanDefinition.getParameterNames();
        assertThat(parameterNames).containsExactly("testPostRepository");
    }

    @Test
    @DisplayName("new BeanDefinition<>(TestJacksonConfig.class, \"testBaseJavaTimeModule\")")
    public void t11() {
        BeanDefinition<JavaTimeModule> beanDefinition = new BeanDefinition<>(TestJacksonConfig.class, "testBaseJavaTimeModule");
        assertThat(beanDefinition.getBeanName()).isEqualTo("testBaseJavaTimeModule");
        assertThat(beanDefinition.getParameterNames()).isEmpty();
        assertThat(beanDefinition.isCreateTypeMethod()).isTrue();
    }

    @Test
    @DisplayName("new BeanDefinition<>(TestJacksonConfig.class, \"testBaseObjectMapper\")")
    public void t12() {
        BeanDefinition<JavaTimeModule> beanDefinition = new BeanDefinition<>(TestJacksonConfig.class, "testBaseObjectMapper");
        assertThat(beanDefinition.getBeanName()).isEqualTo("testBaseObjectMapper");
        assertThat(beanDefinition.getParameterNames()).containsExactly("testBaseJavaTimeModule");
        assertThat(beanDefinition.isCreateTypeMethod()).isTrue();
    }
}