package com.pro_diction.server.domain.search.service;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.member.exception.MemberNotFoundException;
import com.pro_diction.server.domain.member.repository.MemberRepository;
import com.pro_diction.server.domain.search.dto.SearchContentHistoryDto;
import com.pro_diction.server.domain.search.dto.SearchResponseDto;
import com.pro_diction.server.domain.search.entity.Search;
import com.pro_diction.server.domain.search.exception.KeywordRequiredException;
import com.pro_diction.server.domain.search.exception.SearchNotFoundException;
import com.pro_diction.server.domain.search.repository.SearchRepository;
import com.pro_diction.server.domain.study.dto.StudyResponseDto;
import com.pro_diction.server.domain.study.entity.Study;
import com.pro_diction.server.domain.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SearchRepository searchRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SearchContentHistoryDto> getRecentSearchList(Member member) {
        member = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        List<Search> searchList = searchRepository.findAllByMemberOrderById(member);

        return searchList.stream()
                .map(search -> SearchContentHistoryDto.builder()
                        .searchId(search.getId())
                        .searchContent(search.getSearchContent())
                        .searchDate(search.getSearchDate().format(DateTimeFormatter.ofPattern("MM.dd")))    // 날짜 형식 변환
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<StudyResponseDto> getSearchResultStudyList(String keyword, Member member) {
        if(keyword.isEmpty()) throw  new KeywordRequiredException();    // 공백만 입력 or 문자열의 길이가 0
        member = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        List<Study> studyList = studyRepository.findByContentContaining(keyword);

        // 검색 기록 저장
        Search search = Search.builder()
                .searchContent(keyword)
                .member(member)
                .build();
        searchRepository.save(search);

        return studyList.stream()
                .map(study -> StudyResponseDto.builder()
                        .studyId(study.getId())
                        .content(study.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SearchResponseDto deleteSearch(Long searchId) {
        Search search = searchRepository.findById(searchId)
                .orElseThrow(SearchNotFoundException::new);
        searchRepository.delete(search);

        return search.toResponse();
    }
}
