package com.pro_diction.server.domain.member.service;

import com.pro_diction.server.domain.member.dto.LoginResponseDto;
import com.pro_diction.server.domain.member.dto.MemberResponseDto;
import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.member.exception.IdTokenRequiredException;
import com.pro_diction.server.domain.member.exception.MemberNotFoundException;
import com.pro_diction.server.domain.member.repository.MemberRepository;
import com.pro_diction.server.global.common.ApiResponseDto;
import com.pro_diction.server.global.exception.GeneralException;
import com.pro_diction.server.global.util.GoogleOAuthUtil;
import com.pro_diction.server.global.util.JwtUtil;
import com.pro_diction.server.global.util.RedisUtil;
import com.pro_diction.server.global.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final GoogleOAuthUtil googleOAuthUtil;
    private final ResponseUtil responseUtil;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    @Transactional
    public void checkIsUserAndRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralException, GeneralSecurityException {
        Member requestMember = googleOAuthUtil.authenticate(getIdToken(request));
        Member member = memberRepository.findOneByGoogleEmail(requestMember.getGoogleEmail())
                .orElseGet(() -> memberRepository.save(requestMember));
        LoginResponseDto loginResponseDto = jwtUtil.generateTokens(member);
        redisUtil.setData("ID_" + member.getId(), loginResponseDto.getRefreshToken(), Duration.ofDays(14L).toMillis());

        responseUtil.setDataResponse(response, HttpServletResponse.SC_CREATED, loginResponseDto);
    }

    @Override
    @Transactional
    public ApiResponseDto logout(Member member) {
        redisUtil.deleteData("ID_" + member.getId());

        return ApiResponseDto.of(200, "Successful Logout!");
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto getMyProfile(Member member) {
        member = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);

        return MemberResponseDto.toResponse(
                member.getId(), member.getGoogleNickname(), member.getGoogleProfile(), member.getTest().getStage().getTitle(), member.getAge()
        );
    }

    @Override
    @Transactional
    public Integer updateAge(Integer age, Member member) {
        member.update(age);

        return memberRepository.save(member).getAge();
    }

    private String getIdToken(HttpServletRequest request) throws AuthenticationServiceException {
        String idToken = request.getHeader("id-token");

        if (idToken == null)
            throw new IdTokenRequiredException();

        return idToken;
    }
}
