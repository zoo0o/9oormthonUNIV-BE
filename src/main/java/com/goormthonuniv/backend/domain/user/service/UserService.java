package com.goormthonuniv.backend.domain.user.service;

import com.goormthonuniv.backend.domain.user.dto.SignupRequest;
import com.goormthonuniv.backend.domain.user.entity.Role;
import com.goormthonuniv.backend.domain.user.entity.User;
import com.goormthonuniv.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createUser(SignupRequest request) {
        // 1. 아이디 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. 유저 객체 생성
        User user = new User(
                request.getUsername(),
                encodedPassword,
                request.getNickname(),
                Role.USER // 기본값으로 유저 역할
        );

        // 4. 저장
        userRepository.save(user);

        return user.getId();
    }
}
