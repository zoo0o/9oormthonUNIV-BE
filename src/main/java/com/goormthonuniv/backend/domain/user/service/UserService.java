package com.goormthonuniv.backend.domain.user.service;

import com.goormthonuniv.backend.domain.user.dto.SignupRequest;
import com.goormthonuniv.backend.domain.user.type.Role;
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

    /**
     * 회원가입 처리
     */
    @Transactional
    public Long createUser(SignupRequest request) {
        // 아이디 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 유저 엔티티 생성
        User user = new User(
                request.getUsername(),
                encodedPassword,
                request.getNickname(),
                Role.USER // 기본 권한은 일반 사용자
        );

        // 저장
        userRepository.save(user);

        // 유저 ID 반환
        return user.getId();
    }

    /**
     * 유저 조회
     */
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        // username으로 유저를 조회, 없으면 예외 발생
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다: " + username));
    }

}
