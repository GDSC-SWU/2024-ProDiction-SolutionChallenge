package com.pro_diction.server.domain.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private Long memberId;
    private String nickname;
    private String googleProfile;
    private String stage;
    private Integer age;

    public static MemberResponseDto toResponse(
            Long memberId,
            String nickname,
            String googleProfile,
            String stage,
            Integer age
    ) {
        return MemberResponseDto.builder()
                .memberId(memberId)
                .nickname(nickname)
                .googleProfile(googleProfile)
                .stage(stage)
                .age(age)
                .build();
    }
}
