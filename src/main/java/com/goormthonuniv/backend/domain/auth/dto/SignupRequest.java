package com.goormthonuniv.backend.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

    @NotBlank
    private final String username;

    @NotBlank
    @Size(min = 6)
    private final String password;

    @NotBlank
    private final String nickname;

    public SignupRequest(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}
