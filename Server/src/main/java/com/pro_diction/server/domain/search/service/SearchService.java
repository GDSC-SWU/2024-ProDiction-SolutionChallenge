package com.pro_diction.server.domain.search.service;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.search.dto.SearchContentHistoryDto;
import com.pro_diction.server.domain.search.dto.SearchResponseDto;
import com.pro_diction.server.domain.study.dto.StudyResponseDto;

import java.util.List;

public interface SearchService {
    List<SearchContentHistoryDto> getRecentSearchList(Member member);

    List<StudyResponseDto> getSearchResultStudyList(String keyword, Member member);

    SearchResponseDto deleteSearch(Long searchId);
}
