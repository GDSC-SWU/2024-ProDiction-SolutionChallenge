package com.pro_diction.server.domain.search.controller;

import com.pro_diction.server.domain.model.ContextUser;
import com.pro_diction.server.domain.search.dto.SearchContentHistoryDto;
import com.pro_diction.server.domain.search.dto.SearchResponseDto;
import com.pro_diction.server.domain.search.service.SearchService;
import com.pro_diction.server.domain.study.dto.StudyResponseDto;
import com.pro_diction.server.global.common.ApiDataResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search")
@Slf4j
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ApiDataResponseDto<List<SearchContentHistoryDto>> getSearchContentHistoryList(@AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(searchService.getRecentSearchList(contextUser.getMember()));
    }

    @PostMapping
    public ApiDataResponseDto<List<StudyResponseDto>> getSearchResultStudyList(@RequestParam String keyword,
                                                                               @AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(searchService.getSearchResultStudyList(keyword, contextUser.getMember()));
    }

    @DeleteMapping("/{searchId}")
    public ApiDataResponseDto<SearchResponseDto> deleteSearch(@PathVariable Long searchId) {

        return ApiDataResponseDto.of(searchService.deleteSearch(searchId));
    }
}
