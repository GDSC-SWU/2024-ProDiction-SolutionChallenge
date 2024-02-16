package com.pro_diction.server.domain.study.service;

import com.pro_diction.server.domain.study.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyService {
    List<SubCategoryResponseDto> getSubCategoryList(Integer categoryId, Integer studyCount);

    List<StudyResponseDto> getStudyList(Integer subCategoryId, Long parentStudyId);

    DetailStudyResponseDto getDetailStudy(Long id);

    StudyResultDto getStudyResult(MultipartFile multipartFile, Long id);
}