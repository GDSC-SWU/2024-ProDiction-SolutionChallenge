package com.pro_diction.server.domain.member.service;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.member.repository.MemberRepository;
import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;
import com.pro_diction.server.global.util.GoogleOAuthUtil;
import com.pro_diction.server.global.util.JwtUtil;
import com.pro_diction.server.global.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final GoogleOAuthUtil googleOAuthUtil;
    private final MemberRepository memberRepository;
    private final ResponseUtil responseUtil;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void checkIsUserAndRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralException, GeneralSecurityException {
        Member requestMember = googleOAuthUtil.authenticate(getIdToken(request));

        Optional<Member> existingMemberOptional = memberRepository.findOneByGoogleEmail(requestMember.getGoogleEmail());

        if (existingMemberOptional.isPresent()) {
            requestMember = existingMemberOptional.get();
        } else {
            requestMember = memberRepository.save(requestMember);
        }

        responseUtil.setDataResponse(response, HttpServletResponse.SC_CREATED, jwtUtil.generateTokens(requestMember));
    }

    private String getIdToken(HttpServletRequest request) throws AuthenticationServiceException {
        String idToken = request.getHeader("id-token");

        if (idToken == null)
            throw new GeneralException(ErrorCode.ID_TOKEN_REQUIRED);

        return idToken;
    }
}
