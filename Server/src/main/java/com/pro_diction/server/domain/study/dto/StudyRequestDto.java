package com.pro_diction.server.domain.study.dto;

import jakarta.validation.constraints.Null;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRequestDto {
    @Null
    private Integer subCategoryId;

    @Null
    private Long parentStudyId;
}
