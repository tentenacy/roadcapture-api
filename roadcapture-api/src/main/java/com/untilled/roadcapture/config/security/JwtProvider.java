package com.untilled.roadcapture.config.security;

import com.untilled.roadcapture.api.dto.token.TokenResponse;
import com.untilled.roadcapture.api.exception.security.CSecurityException.CAuthenticationEntryPointException;
import com.untilled.roadcapture.api.service.security.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    public final String ROLES = "roles";
    private final Long accessTokenValidMillisecond = 60 * 60 * 1000L; //1 hour
    private final Long refreshTokenValidMillisecond = 1000 * 365 * 24 * 60 * 60 * 1000L; //1000 years
    private final CustomUserDetailsService userDetailsService;
    @Value("spring.jwt.secret")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Jwt 생성
     */
    public TokenResponse createToken(String userPk, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put(ROLES, roles);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return new TokenResponse("bearer", accessToken, refreshToken, accessTokenValidMillisecond);
    }

    /**
     * Jwt로 인증정보를 조회
     */
    public Authentication getAuthentication(String token) {

        Claims claims = parseClaims(token);

        if (ObjectUtils.isEmpty(claims.get(ROLES))) {
            throw new CAuthenticationEntryPointException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 만료된 토큰이어도 refresh token을 검증 후 재발급할 수 있도록 claims 반환
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * HTTP Request의 Header에서 Token Parsing -> "X-AUTH-TOKEN: jwt"
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * Jwt의 유효성 및 만료일자 확인
     */
    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error(e.toString());
            return false;
        }
    }
}
