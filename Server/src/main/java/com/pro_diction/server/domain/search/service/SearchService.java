package com.pro_diction.server.domain.search.service;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.study.dto.StudyResponseDto;

import java.util.List;

public interface SearchService {
    List<StudyResponseDto> getSearchResultStudyList(String keyword, Member member);
}
