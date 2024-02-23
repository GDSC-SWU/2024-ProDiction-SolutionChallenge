package com.pro_diction.server.domain.vocabulary.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyResponseDto {
    private Long vocabularyId;
    private Long studyId;
    private String content;
}
