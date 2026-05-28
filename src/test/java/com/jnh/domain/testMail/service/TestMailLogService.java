package com.jnh.domain.testMail.service;

import com.jnh.domain.testMail.repository.TestMailLogRepository;
import com.jnh.domain.testPost.testPost.service.TestFacadePostService;
import com.jnh.framework.annotations.Autowired;
import com.jnh.framework.annotations.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
public class TestMailLogService {
    private final TestFacadePostService testFacadePostService;
    private final TestMailLogRepository testMailLogRepository;
    private final List<String> testSafeExts;

    public TestMailLogService(TestFacadePostService testFacadePostService, TestMailLogRepository testMailLogRepository) {
        this.testFacadePostService = testFacadePostService;
        this.testMailLogRepository = testMailLogRepository;
        this.testSafeExts = null;
    }

    @Autowired
    public TestMailLogService(TestFacadePostService testFacadePostService, TestMailLogRepository testMailLogRepository, List<String> testSafeExts) {
        this.testFacadePostService = testFacadePostService;
        this.testMailLogRepository = testMailLogRepository;
        this.testSafeExts = testSafeExts;
    }
}
