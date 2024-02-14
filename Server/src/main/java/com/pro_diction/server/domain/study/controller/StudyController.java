package com.pro_diction.server.domain.study.controller;

import com.pro_diction.server.domain.study.dto.*;
import com.pro_diction.server.domain.study.service.StudyService;
import com.pro_diction.server.global.common.ApiDataResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/study")
@Slf4j
public class StudyController {
    private final StudyService studyService;

    @GetMapping("/subcategory")
    public ApiDataResponseDto<List<SubCategoryResponseDto>> getSubCategoryList(@RequestParam @NotNull Integer categoryId,
                                                                               @RequestParam @NotNull boolean isFinalConsonant,
                                                                               @RequestParam @NotNull Integer studyCount) {

        return ApiDataResponseDto.of(studyService.getSubCategoryList(categoryId, isFinalConsonant, studyCount));
    }

    @GetMapping
    public ApiDataResponseDto<List<StudyResponseDto>> getStudyList(@RequestParam(required = false) Integer subCategoryId,
                                                                   @RequestParam(required = false) Long parentStudyId) {

        return ApiDataResponseDto.of(studyService.getStudyList(subCategoryId,parentStudyId));
    }

    @GetMapping("/{id}")
    public ApiDataResponseDto<DetailStudyResponseDto> getDetailStudy(@PathVariable @NotNull Long id) {

        return ApiDataResponseDto.of(studyService.getDetailStudy(id));
    }

    @PostMapping
    public ApiDataResponseDto<StudyResultDto> getStudyResult(@RequestPart @NotNull MultipartFile multipartFile,
                                                             @RequestParam @NotNull Long id) throws IOException {

        return ApiDataResponseDto.of(studyService.getStudyResult(multipartFile, id));
    }
}
