package com.pro_diction.server.domain.member.service;

import com.pro_diction.server.domain.member.dto.MemberResponseDto;
import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.global.common.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface MemberService {
    void checkIsUserAndRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException;

    ApiResponseDto logout(Member member);

    MemberResponseDto getMyProfile(Member member);

    Integer updateAge(Integer age, Member member);
}
