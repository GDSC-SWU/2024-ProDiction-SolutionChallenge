package com.pro_diction.server.domain.search.service;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.member.repository.MemberRepository;
import com.pro_diction.server.domain.search.entity.Search;
import com.pro_diction.server.domain.search.repository.SearchRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {
    @InjectMocks
    private SearchServiceImpl searchService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SearchRepository searchRepository;

    @Test
    @DisplayName("필요없는 검색 기록 스케줄링 삭제 테스트")
    public void cleanSearchHistory() {
        // 가짜 Member 리스트 생성
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            members.add(new Member());
        }
        when(memberRepository.findAll()).thenReturn(members);

        // 가짜 Search 리스트 생성
        List<Search> searches = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            searches.add(new Search());
        }
        when(searchRepository.findAllByMemberOrderByIdDesc(any())).thenReturn(searches);

        // cleanSearchHistory() 메서드 호출
        searchService.cleanSearchHistory();

        // Member별로 호출된 Search의 수 확인
        ArgumentCaptor<List<Search>> captor = ArgumentCaptor.forClass(List.class);
        // Repository가 잘 호출되었는지 확인
        verify(searchRepository, times(3)).deleteAll(captor.capture());

        // 남아야하는 search 수 검증
        List<List<Search>> allValues = captor.getAllValues();
        for (List<Search> searchList : allValues) {
            assertEquals(searchList.size(), 8);
        }
    }
}