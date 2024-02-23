package com.pro_diction.server.domain.vocabulary.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveVocabularyResponseDto {
    private Long vocabularyId;
    private Long studyId;
    private Long memberId;
}
