package com.pro_diction.server.domain.vocabulary.service;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.vocabulary.dto.SaveVocabularyResponseDto;

public interface VocabularyService {
    SaveVocabularyResponseDto saveVocabulary(Long id, Member member);
}
