package com.goormthonuniv.backend.global.config;

import com.goormthonuniv.backend.global.jwt.JwtAuthenticationEntryPoint;
import com.goormthonuniv.backend.global.jwt.JwtAuthenticationFilter;
import com.goormthonuniv.backend.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint entryPoint;

    // 인증 없이 접근 가능한 Swagger 경로 목록
    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    /**
     * Spring Security 필터 체인 설정
     * - 세션을 사용하지 않고 JWT 기반 인증 방식 설정
     * - 특정 경로에 대한 접근 권한 지정
     * - JWT 인증 필터 적용
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 기능 비활성화
                .csrf(csrf -> csrf.disable())

                // 세션 미사용 설정
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청별 접근 제어 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()         // Swagger 접근 허용
                        .requestMatchers("/api/auth/**").permitAll()            // 인증 관련 API 허용
                        .requestMatchers("/api/users/signup").permitAll()       // 회원가입 허용
                        .anyRequest().authenticated()                           // 그 외 요청은 인증 필요
                )

                // 인증 실패 시 예외 처리
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                )

                // JWT 인증 필터 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 비밀번호 암호화 설정
     * - BCrypt 해시 알고리즘 사용
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager 빈 등록
     * - 로그인 인증 처리 시 사용
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
