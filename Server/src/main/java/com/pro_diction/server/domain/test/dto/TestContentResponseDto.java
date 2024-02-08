package com.pro_diction.server.domain.test.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestContentResponseDto {
    private Long studyId;
    private String content;
}
