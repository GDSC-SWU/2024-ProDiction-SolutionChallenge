package com.pro_diction.server.global.util.filter;

import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.model.ContextUser;
import com.pro_diction.server.global.exception.GeneralException;
import com.pro_diction.server.global.util.JwtUtil;
import com.pro_diction.server.global.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ResponseUtil responseUtil;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws GeneralException, ServletException, IOException, ExpiredJwtException {
        final String TOKEN_REFRESH_API_URL = "/api/v1/member/refresh";
        final String LOGIN_API_URL = "/api/v1/member/login/oauth/google";

        String uri = request.getRequestURI();
        if (uri.equals(LOGIN_API_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 재발급 요청이 올 경우
        if(uri.equals(TOKEN_REFRESH_API_URL)) {
            String refreshToken = jwtUtil.decodeHeader(false, request);
            Member member = jwtUtil.getMember(refreshToken);
            saveAuthentication(member);
            responseUtil.setDataResponse(response, HttpServletResponse.SC_CREATED, jwtUtil.generateTokens(member));

            return;
        }

        String accessToken = jwtUtil.decodeHeader(true, request);
        Member member = jwtUtil.getMember(accessToken);

        saveAuthentication(member);
        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(Member member) {
        ContextUser contextUser = new ContextUser(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                contextUser,
                contextUser.getPassword(),
                authoritiesMapper.mapAuthorities(contextUser.getAuthorities()));

        // context 초기화 후 인증 정보 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}