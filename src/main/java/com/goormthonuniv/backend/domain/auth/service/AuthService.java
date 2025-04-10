package com.goormthonuniv.backend.domain.auth.service;

import com.goormthonuniv.backend.domain.auth.dto.LoginRequest;
import com.goormthonuniv.backend.domain.auth.dto.LoginResponse;
import com.goormthonuniv.backend.domain.user.entity.User;
import com.goormthonuniv.backend.domain.user.repository.UserRepository;
import com.goormthonuniv.backend.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponse login(LoginRequest request) {
        // 1. 유저 조회
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 생성
        String token = jwtProvider.generateToken(user.getId(), user.getRole().getKey());

        // 4. 응답 반환
        return new LoginResponse(token);
    }
}
