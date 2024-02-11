package com.pro_diction.server.domain.study.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryRequestDto {
    @NotNull
    private Integer categoryId;

    @NotNull
    private boolean isFinalConsonant;

    @NotNull
    private Integer studyCount;
}
