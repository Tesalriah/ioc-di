package com.jnh.global.testFile;

import com.jnh.domain.testFile.testFile.service.TestFileService;
import com.jnh.framework.annotations.Bean;
import com.jnh.framework.annotations.Configuration;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TestFileConfig {
    private final TestFileService testFileService;

    @Bean
    public List<String> testSafeExts() {
        return List.of("jpg", "jpeg", "png", "gif", "bmp");
    }
}