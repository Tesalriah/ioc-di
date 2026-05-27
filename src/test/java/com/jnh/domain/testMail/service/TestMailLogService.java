package com.jnh.domain.testMail.service;

import com.jnh.domain.testMail.repository.TestMailLogRepository;
import com.jnh.domain.testPost.testPost.service.TestFacadePostService;
import com.jnh.framework.annotations.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestMailLogService {
    private final TestFacadePostService testFacadePostService;
    private final TestMailLogRepository testMailLogRepository;
    private final List<String> testSafeExts;
}
