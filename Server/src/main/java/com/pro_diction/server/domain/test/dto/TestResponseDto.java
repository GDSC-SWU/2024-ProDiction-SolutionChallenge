package com.pro_diction.server.domain.test.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestResponseDto {
    private Long studyId;
    private Double score;
}
