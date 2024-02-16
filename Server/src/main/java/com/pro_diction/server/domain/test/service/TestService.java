package com.pro_diction.server.domain.test.service;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.test.dto.TestContentResponseDto;
import com.pro_diction.server.domain.test.dto.TestResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TestService {
    List<TestContentResponseDto> getTestStudyList(Integer categoryId);

    TestResponseDto testDiction(MultipartFile multipartFile, Long id) throws IOException;

    Integer saveOrUpdateStage(Integer stage, Member member);
}
