package com.pro_diction.server.domain.test.controller;

import com.pro_diction.server.domain.test.dto.TestResponseDto;
import com.pro_diction.server.domain.test.service.TestService;
import com.pro_diction.server.global.common.ApiDataResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/test")
@Slf4j
public class TestController {
    private final TestService testService;

    @GetMapping
    public ApiDataResponseDto<TestResponseDto> test(@RequestPart MultipartFile multipartFile,
                                                    @RequestParam Long id) throws IOException {
        return ApiDataResponseDto.of(testService.testDiction(multipartFile, id));
    }
}
