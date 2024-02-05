package com.pro_diction.server.domain.test.service;

import com.pro_diction.server.domain.test.dto.TestResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TestService {
    TestResponseDto testWithStep(MultipartFile multipartFile, Long id) throws IOException;
}
