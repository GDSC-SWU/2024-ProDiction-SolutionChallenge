package com.pro_diction.server.global.util;

import com.pro_diction.server.domain.member.dto.LoginResponseDto;
import com.pro_diction.server.domain.member.entity.Member;
import com.pro_diction.server.domain.member.repository.MemberRepository;
import com.pro_diction.server.domain.model.TokenClaimVo;
import com.pro_diction.server.global.constant.ErrorCode;
import com.pro_diction.server.global.exception.GeneralException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    private final MemberRepository memberRepository;

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
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes()))
                .compact();
    }

    // Validate Token
    public Member validateToken(String token) throws GeneralException {
        // 검증 및 payload 추출
        Map<String, Object> payloads = parser()
                .setSigningKey(JWT_SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();

        // member 조회
        return memberRepository.findById(((Number) payloads.get("memberId")).longValue())
                .orElseThrow(() -> new GeneralException(ErrorCode.INVALID_TOKEN));
    }

    // Decode Request
    public String decodeHeader(boolean isAccessToken, HttpServletRequest request) {
        final String ACCESS_HEADER = "Authorization";
        final String REFRESH_HEADER = "Authorization-refresh";
        final String header = isAccessToken ? ACCESS_HEADER : REFRESH_HEADER;

        String header_value = request.getHeader(header);
        if(isAccessToken && header_value == null) {
            throw new GeneralException(ErrorCode.ACCESS_TOKEN_REQUIRED);
        } else if (header_value == null) {
            return null;
        }

        return decodeBearer(header_value);
    }

    // Decode Bearer
    public String decodeBearer(String bearer_token) throws GeneralException {
        try {
            final String BEARER = "Bearer ";
            return Arrays.stream(bearer_token.split(BEARER)).toList().get(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new GeneralException(ErrorCode.INVALID_TOKEN);
        }
    }
}
