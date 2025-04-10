package com.goormthonuniv.backend.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 생성 및 검증 담당 컴포넌트
 */
@Component
public class JwtProvider {

    @Value("${JWT_SECRET}")
    private String secretKeyString; // application 환경변수에서 주입받은 시크릿 키

    private Key secretKey;
    private final long tokenValidity = 1000 * 60 * 60; // 토큰 유효시간: 1시간

    /**
     * 빈 초기화 후 실행되는 메서드
     * - 시크릿 키를 HMAC 알고리즘용 Key 객체로 변환
     */
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    /**
     * 사용자 ID와 역할(Role)을 바탕으로 JWT 생성
     * - issuedAt : 생성 시간
     * - expiration : 만료 시간
     */
    public String generateToken(Long userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenValidity);

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // subject는 사용자 식별자
                .claim("role", role) // 커스텀 클레임: 역할
                .setIssuedAt(now) // 발급 시간
                .setExpiration(expiry) // 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256) // 서명
                .compact(); // 최종 JWT 문자열 생성
    }

    /**
     * 토큰 유효성 검사
     * - 파싱 중 예외 발생 시 유효하지 않다고 판단
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) // 서명 검증을 위한 키 지정
                    .build()
                    .parseClaimsJws(token);   // 토큰 파싱 시도
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 만료, 변조 등 예외 발생 시 false
        }
    }

    /**
     * 토큰에서 사용자 이름(username) 추출
     */
    public Long getUserIdFromToken(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token) // 토큰 파싱
                .getBody()
                .getSubject();

        return Long.parseLong(subject); // subject = userId (String)
    }


    /**
     * 토큰에서 사용자 역할(role) 추출
     */
    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}