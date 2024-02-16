package com.pro_diction.server.domain.study.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyResultDto {
    private Long studyId;
    private String sttResult;
    private String splitSttResult;

    public static StudyResultDto toResponse(
            Long studyId,
            String sttResult,
            String splitSttResult
    ) {
        return StudyResultDto.builder()
                .studyId(studyId)
                .sttResult(sttResult)
                .splitSttResult(splitSttResult)
                .build();
    }
}
