package com.jnh.domain.testPost.testPost.service;

import com.jnh.domain.testPost.testPost.repository.TestPostRepository;
import com.jnh.framework.annotations.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestFacadePostService {
    private final TestPostService testPostService;
    private final TestPostRepository testPostRepository;
}
