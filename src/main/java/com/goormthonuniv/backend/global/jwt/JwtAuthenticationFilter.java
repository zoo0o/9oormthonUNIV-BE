package com.goormthonuniv.backend.global.jwt;

import com.goormthonuniv.backend.domain.auth.service.CustomUserDetailsService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 인증 필터
 * - 요청당 한 번 실행됨 (OncePerRequestFilter 상속)
 * - JWT 토큰을 검증하고, 인증 정보를 SecurityContext에 저장함
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * 필터 내부 로직 처리 메서드
     * - 1. Authorization 헤더에서 토큰 추출
     * - 2. 토큰 유효성 검증
     * - 3. 사용자 정보 추출 (userId, role)
     * - 4. 인증 객체 생성 및 SecurityContext에 저장
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization 헤더에서 토큰 추출 (형식: "Bearer {token}")
        String token = resolveToken(request);

        // 2. 토큰이 존재하고, 유효한 경우에만 인증 처리
        if (token != null && jwtProvider.validateToken(token)) {
            // 3. 토큰에서 사용자 ID, 역할(Role) 추출
            String username = jwtProvider.getUsernameFromToken(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // 4. 인증 객체 생성 (Spring Security에서 사용할 인증 정보)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,                 // principal
                            null,                        // credentials
                            userDetails.getAuthorities() // 권한
                    );

            // 5. 인증 객체에 요청 정보 부여 (IP, 세션 등)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. SecurityContext에 인증 정보 저장 (로그인 상태 유지)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 7. 다음 필터로 요청 전달 (다음 필터 or 컨트롤러)
        filterChain.doFilter(request, response);
    }

    /**
     * 요청 헤더에서 JWT 토큰 추출
     * - 형식: Authorization: Bearer {token}
     */
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        // "Bearer "로 시작하면 그 뒤가 토큰
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer " 제거 후 토큰만 리턴
        }

        return null;
    }
}
