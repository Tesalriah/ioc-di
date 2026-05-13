package com.jnh.domain.testPost.testPost.service;

import com.jnh.domain.testPost.testPost.repository.TestPostRepository;

public class TestPostService {
    private TestPostRepository testPostRepository;

    public TestPostService(TestPostRepository testPostRepository){
        this.testPostRepository = testPostRepository;
    }
}
