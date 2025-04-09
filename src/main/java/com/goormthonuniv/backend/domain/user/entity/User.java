package com.goormthonuniv.backend.domain.user.entity;

import com.goormthonuniv.backend.domain.auth.dto.SignupRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    public static User from(SignupRequest dto, PasswordEncoder encoder) {
        User user = new User();
        user.username = dto.getUsername();
        user.password = encoder.encode(dto.getPassword());
        user.nickname = dto.getNickname();
        return user;
    }
}
