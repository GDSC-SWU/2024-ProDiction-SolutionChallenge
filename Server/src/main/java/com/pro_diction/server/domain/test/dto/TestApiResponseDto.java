package com.pro_diction.server.domain.test.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestApiResponseDto {
    private String result;
    private String return_type;
    private TestApiResponseObjectDto return_object;
}
