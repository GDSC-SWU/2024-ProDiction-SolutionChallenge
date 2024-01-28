package com.pro_diction.server.domain.member.service;

import com.pro_diction.server.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface MemberService {
    void checkIsUserAndRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException;

    Integer updateAge(Integer age, Member member);
}
