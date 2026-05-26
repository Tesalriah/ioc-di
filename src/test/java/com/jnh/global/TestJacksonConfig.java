package com.jnh.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jnh.framework.annotations.Bean;
import com.jnh.framework.annotations.Configuration;

@Configuration
public class TestJacksonConfig {
    @Bean
    public JavaTimeModule testBaseJavaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public ObjectMapper testBaseObjectMapper(JavaTimeModule testBaseJavaTimeModule) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(testBaseJavaTimeModule);
        return objectMapper;
    }
}