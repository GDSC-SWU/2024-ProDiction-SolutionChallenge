package com.pro_diction.server.domain.test.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class TestApiResponseObjectDto {
    private String recognized;
    private String score;
}
