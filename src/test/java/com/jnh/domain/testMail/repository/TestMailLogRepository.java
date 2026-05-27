package com.jnh.domain.testMail.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jnh.framework.annotations.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TestMailLogRepository {
    private final ObjectMapper testBaseObjectMapper;
    private final JavaTimeModule testBaseJavaTimeModule;
}