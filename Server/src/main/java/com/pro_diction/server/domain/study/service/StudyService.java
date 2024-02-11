package com.pro_diction.server.domain.study.service;

import com.pro_diction.server.domain.study.dto.StudyRequestDto;
import com.pro_diction.server.domain.study.dto.StudyResponseDto;
import com.pro_diction.server.domain.study.dto.SubCategoryRequestDto;
import com.pro_diction.server.domain.study.dto.SubCategoryResponseDto;

import java.util.List;

public interface StudyService {
    List<SubCategoryResponseDto> getSubCategoryList(SubCategoryRequestDto request);

    List<StudyResponseDto> getStudyList(StudyRequestDto request);
}