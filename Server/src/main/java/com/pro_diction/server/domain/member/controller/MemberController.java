package com.pro_diction.server.domain.member.controller;

import com.pro_diction.server.domain.member.service.MemberService;
import com.pro_diction.server.global.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value = "/login/oauth/google")
    public void login(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws IOException, GeneralSecurityException {

        memberService.checkIsUserAndRegister(request, response);
    }
}
