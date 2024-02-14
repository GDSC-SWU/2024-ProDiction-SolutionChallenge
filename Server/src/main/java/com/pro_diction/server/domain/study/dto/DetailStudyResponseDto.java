package com.pro_diction.server.domain.study.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailStudyResponseDto {
    private Long studyId;
    private String content;
    private String splitPronunciation;
}
