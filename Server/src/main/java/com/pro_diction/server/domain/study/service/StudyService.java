package com.pro_diction.server.domain.study.service;

import com.pro_diction.server.domain.study.dto.StudyResponseDto;
import com.pro_diction.server.domain.study.dto.SubCategoryResponseDto;

import java.util.List;

public interface StudyService {
    List<SubCategoryResponseDto> getSubCategoryList(Integer categoryId, boolean isFinalConsonant);

    List<StudyResponseDto> getStudyList(Integer subCategoryId, Long parentStudyId);
}