package com.pro_diction.server.domain.study.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryResponseDto {
    private Integer id;
    private String name;
    private List<StudyResponseDto> studyResponseDtoList;
}
