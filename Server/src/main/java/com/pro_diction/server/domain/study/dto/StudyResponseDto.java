package com.pro_diction.server.domain.study.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyResponseDto {
    private Long studyId;
    private String content;
}
