package com.goormthonuniv.backend.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
