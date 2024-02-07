package com.pro_diction.server.domain.test.service;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.model.Stage;
import com.pro_diction.server.domain.study.entity.Study;
import com.pro_diction.server.domain.study.exception.StudyNotFoundException;
import com.pro_diction.server.domain.study.repository.StudyRepository;
import com.pro_diction.server.domain.test.dto.*;
import com.pro_diction.server.domain.test.entity.Test;
import com.pro_diction.server.domain.test.exception.InvalidStageException;
import com.pro_diction.server.domain.test.repository.TestRepository;
import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final StudyRepository studyRepository;
    private final TestRepository testRepository;

    @Value("${DICTION_TEST_API_URL}")
    private String DICTION_TEST_API_URL;

    @Value("${DICTION_TEST_API_KEY}")
    private String DICTION_TEST_API_KEY;

    @Override
    @Transactional(readOnly = true)
    public TestResponseDto testDiction(MultipartFile multipartFile, Long id) throws IOException {
        Study study = studyRepository.findById(id).orElseThrow(() -> new StudyNotFoundException());
        Double score = test(multipartFile, study.getPronunciation());

        return TestResponseDto.builder()
                .studyId(id)
                .score(score)
                .build();
    }

    @Override
    @Transactional
    public Integer saveOrUpdateStage(Integer stage, Member member) {    // 처음이라면 save, 존재한다면 update
        if (!(stage > 0 && stage < 6))   throw new InvalidStageException();

        Test test = testRepository.findOneByMember(member).orElse(new Test(member));
        test.update(Stage.getByLevel(stage));
        testRepository.save(test);

        return stage;
    }

    private Double test(MultipartFile file, String pronunciation) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        String audioContents = null;
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        // .wav 음성 파일을 Base64로 인코딩
        byte[] audioBytes = file.getBytes();
        audioContents = Base64.getEncoder().encodeToString(audioBytes);

        // Request Header 구성
        httpHeaders.set("Authorization", DICTION_TEST_API_KEY);

        // Request Body 구성
        argument.put("language_code", "korean");
        argument.put("script", pronunciation);
        argument.put("audio", audioContents);
        request.put("argument", argument);

        // Request Header, Body 요청 구성
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, httpHeaders);

        TestApiResponseDto testApiResponseDto =
                restTemplate.postForEntity(DICTION_TEST_API_URL, httpEntity, TestApiResponseDto.class).getBody();
        try {
            return Math.round(Double.valueOf(testApiResponseDto.getReturn_object().getScore()) / 5 * 100 * 10) / 10.0;
        } catch (NullPointerException e) {
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
