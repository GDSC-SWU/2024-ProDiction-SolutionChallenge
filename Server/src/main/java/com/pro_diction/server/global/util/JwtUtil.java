package com.pro_diction.server.global.util;

import com.pro_diction.server.domain.member.dto.LoginResponseDto;
import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.member.repository.MemberRepository;
import com.pro_diction.server.domain.model.TokenClaimVo;
import com.pro_diction.server.global.exception.auth.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.*;

import static io.jsonwebtoken.Jwts.*;
import static io.jsonwebtoken.Jwts.parserBuilder;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    @Value("${jwt.issuer}")
    private String ISSUER;

    @Value("${jwt.secret}")
    private String JWT_SECRET_KEY;

    public LoginResponseDto generateTokens(Member member) {
        TokenClaimVo vo = new TokenClaimVo(member.getId(), member.getGoogleEmail());

        // 토큰 발급
        String accessToken = generateToken(true, vo);
        String refreshToken = generateToken(false, vo);

        return new LoginResponseDto(member.getId(), accessToken, refreshToken);
    }

    // 토큰 생성
    public String generateToken(boolean isAccessToken, TokenClaimVo vo) {
        // Payloads 생성
        Map<String, Object> payloads = new LinkedHashMap<>();
        payloads.put("memberId", vo.getId());
        payloads.put("email", vo.getEmail());

        // Expiration time (access: 30m / refresh: 14d)
        Date now = new Date();
        Duration duration = isAccessToken ? Duration.ofMinutes(30) : Duration.ofDays(14);
        Date expiration = new Date(now.getTime() + duration.toMillis());

        // Subject
        String subject = isAccessToken ? "access" : "refresh";

        // Build
        return builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(payloads)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(subject)
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Member getMember(String token) throws ExpiredJwtException {
        // 검증 및 payload 추출
        Map<String, Object> payloads = parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // member 조회
        return memberRepository.findById(((Number) payloads.get("memberId")).longValue())
                .orElseThrow((InvalidTokenException::new));
    }

    public Member validateRefreshToken(String token) throws ExpiredJwtException {
        // 검증 및 payload 추출
        Map<String, Object> payloads = parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Member member = memberRepository.findById(((Number) payloads.get("memberId")).longValue())
                .orElseThrow((InvalidTokenException::new));

        String refreshToken = redisUtil.getData("ID_" + member.getId());

        if(refreshToken == null) {
            throw new LoginRequiredException();
        }else if(!refreshToken.equals(token)) {
            throw new AuthorizationFailedException();
        }

        return member;
    }

    // Decode Request
    public String decodeHeader(boolean isAccessToken, HttpServletRequest request) {
        final String ACCESS_HEADER = "Authorization";
        final String REFRESH_HEADER = "Authorization-refresh";
        final String header = isAccessToken ? ACCESS_HEADER : REFRESH_HEADER;

        String headerValue = request.getHeader(header);
        if(isAccessToken && headerValue == null) {
            throw new AccessTokenRequiredException();
        } else if(!isAccessToken && headerValue == null) {
            throw new RefreshTokenRequiredException();
        }

        return decodeBearer(headerValue);
    }

    // Decode Bearer
    public String decodeBearer(String bearerToken) {
        try {
            final String BEARER = "Bearer ";
            return Arrays.stream(bearerToken.split(BEARER)).toList().get(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidTokenException();
        }
    }

    // secretKey 로드
    private Key getSigninKey() {
        byte[] keyBytes = JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

