package com.pro_diction.server.domain.vocabulary.service;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.vocabulary.dto.SaveVocabularyResponseDto;
import com.pro_diction.server.domain.vocabulary.dto.VocabularyResponseDto;

import java.util.List;

public interface VocabularyService {
    List<VocabularyResponseDto> getVocabularyList(Integer categoryId, Member member);

    SaveVocabularyResponseDto saveVocabulary(Long studyId, Member member);

    SaveVocabularyResponseDto deleteVocabulary(Long vocabularyId, Member member);
}
