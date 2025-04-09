package com.goormthonuniv.backend.global.jwt;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 * 요청당 한 번 실행되며, JWT 토큰을 검증하고 인증 정보를 SecurityContext에 저장함
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    /**
     * 실제 필터링 로직
     * - Authorization 헤더에서 토큰 추출
     * - 토큰 유효성 검증
     * - 인증 객체 생성 후 SecurityContext에 저장
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 헤더에서 토큰 추출
        String token = resolveToken(request);

        // 토큰이 유효하면
        if (token != null && jwtProvider.validateToken(token)) {
            // 토큰에서 사용자명 추출
            String username = jwtProvider.getUsernameFromToken(token);

            // 인증 객체 생성 (권한은 현재 없음)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, null);

            // 요청 정보와 함께 인증 객체에 상세 정보 설정
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 인증 정보 저장 (로그인된 사용자로 인식됨)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // 헤더가 "Bearer {토큰}" 형식인지 확인 후 토큰만 잘라냄
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
