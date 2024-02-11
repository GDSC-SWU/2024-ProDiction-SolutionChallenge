package com.pro_diction.server.domain.study.controller;

import com.pro_diction.server.domain.study.dto.StudyRequestDto;
import com.pro_diction.server.domain.study.dto.StudyResponseDto;
import com.pro_diction.server.domain.study.dto.SubCategoryRequestDto;
import com.pro_diction.server.domain.study.dto.SubCategoryResponseDto;
import com.pro_diction.server.domain.study.service.StudyService;
import com.pro_diction.server.global.common.ApiDataResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/study")
@Slf4j
public class StudyController {
    private final StudyService studyService;

    @GetMapping("/subcategory")
    public ApiDataResponseDto<List<SubCategoryResponseDto>> getSubCategoryList(@RequestBody SubCategoryRequestDto requestDto) {

        return ApiDataResponseDto.of(studyService.getSubCategoryList(requestDto.getCategoryId(), requestDto.isFinalConsonant(), requestDto.getStudyCount()));
    }

    @GetMapping("/subcategory/study")
    public ApiDataResponseDto<List<StudyResponseDto>> getStudyList(@RequestBody StudyRequestDto requestDto) {

        return ApiDataResponseDto.of(studyService.getStudyList(requestDto.getSubCategoryId(), requestDto.getParentStudyId()));
    }
}
