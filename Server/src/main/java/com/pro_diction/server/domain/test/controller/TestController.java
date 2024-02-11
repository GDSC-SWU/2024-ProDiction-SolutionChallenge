package com.pro_diction.server.domain.test.controller;

import com.pro_diction.server.domain.model.ContextUser;
import com.pro_diction.server.domain.test.dto.TestContentResponseDto;
import com.pro_diction.server.domain.test.dto.TestResponseDto;
import com.pro_diction.server.domain.test.service.TestService;
import com.pro_diction.server.global.common.ApiDataResponseDto;
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

    @GetMapping("/study")
    public ApiDataResponseDto<List<TestContentResponseDto>> getTestStudyList(@RequestParam Integer stage) {

        return ApiDataResponseDto.of(testService.getTestStudyList(stage));
    }

    @GetMapping
    public ApiDataResponseDto<TestResponseDto> test(@RequestPart MultipartFile multipartFile,
                                                    @RequestParam Long id) throws IOException {

        return ApiDataResponseDto.of(testService.testDiction(multipartFile, id));
    }

    @PostMapping
    public Integer saveOrUpdateStage(@RequestParam Integer stage,
                                     @AuthenticationPrincipal ContextUser contextUser) {

        return testService.saveOrUpdateStage(stage, contextUser.getMember());
    }
}