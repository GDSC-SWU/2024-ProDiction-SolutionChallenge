package com.pro_diction.server.domain.study.service;

import com.pro_diction.server.domain.study.dto.*;

import java.util.List;

public interface StudyService {
    List<SubCategoryResponseDto> getSubCategoryList(SubCategoryRequestDto request);

    List<StudyResponseDto> getStudyList(StudyRequestDto request);

    DetailStudyResponseDto getDetailStudy(Long id);
}