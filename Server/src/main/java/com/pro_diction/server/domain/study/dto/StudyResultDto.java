package com.pro_diction.server.domain.study.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyResultDto {
    private Long studyId;
    private String content;
    private String pronunciation;
    private String splitPronunciation;
    private Double score;
    private String sttResult;
    private String splitSttResult;

    public static StudyResultDto toResponse(
            Long studyId,
            Double score,
            String sttResult,
            String splitSttResult
    ) {
        return StudyResultDto.builder()
                .studyId(studyId)
                .score(score)
                .sttResult(sttResult)
                .splitSttResult(splitSttResult)
                .build();
    }
}
