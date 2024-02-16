package com.pro_diction.server.domain.test.controller;

import com.pro_diction.server.domain.model.ContextUser;
import com.pro_diction.server.domain.test.dto.TestContentResponseDto;
import com.pro_diction.server.domain.test.dto.TestResponseDto;
import com.pro_diction.server.domain.test.service.TestService;
import com.pro_diction.server.global.common.ApiDataResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/test")
@Slf4j
public class TestController {
    private final TestService testService;

    @GetMapping("/{categoryId}")
    public ApiDataResponseDto<List<TestContentResponseDto>> getTestStudyList(@PathVariable @NotNull Integer categoryId) {

        return ApiDataResponseDto.of(testService.getTestStudyList(categoryId));
    }

    @PostMapping("/diction/{studyId}")
    public ApiDataResponseDto<TestResponseDto> test(@RequestPart @NotNull MultipartFile multipartFile,
                                                    @PathVariable @NotNull Long studyId) throws IOException {

        return ApiDataResponseDto.of(testService.testDiction(multipartFile, studyId));
    }

    @PostMapping
    public Integer saveOrUpdateStage(@RequestParam Integer stage,
                                     @AuthenticationPrincipal ContextUser contextUser) {

        return testService.saveOrUpdateStage(stage, contextUser.getMember());
    }
}
