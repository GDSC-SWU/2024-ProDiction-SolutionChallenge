package com.pro_diction.server.domain.search.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDto {
    private Long searchId;
    private String searchContent;
    private Long memberId;
}
