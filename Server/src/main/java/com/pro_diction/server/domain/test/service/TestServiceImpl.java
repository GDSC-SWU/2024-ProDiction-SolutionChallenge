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
import com.pro_diction.server.global.util.DictionTestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final StudyRepository studyRepository;
    private final TestRepository testRepository;
    private final DictionTestUtil dictionTestUtil;

    @Override
    @Transactional(readOnly = true)
    public List<TestContentResponseDto> getTestStudyList(Integer stage) {
        if (!(stage > 0 && stage < 6))   throw new InvalidStageException();
        List<Study> studyList = studyRepository.getStudyByCategoryId(stage);

        return studyList.stream()
                .map(study -> TestContentResponseDto.builder()
                        .studyId(study.getId())
                        .content(study.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public TestResponseDto testDiction(MultipartFile multipartFile, Long id) throws IOException {
        Study study = studyRepository.findById(id).orElseThrow(StudyNotFoundException::new);
        Integer score = dictionTestUtil.test(multipartFile, study.getPronunciation());

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
}
