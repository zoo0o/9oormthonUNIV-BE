package com.goormthonuniv.backend.domain.auth.controller;

import com.goormthonuniv.backend.domain.auth.dto.LoginRequest;
import com.goormthonuniv.backend.domain.auth.dto.LoginResponse;
import com.goormthonuniv.backend.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 입력하여 로그인을 시도합니다. 성공 시 JWT 토큰이 반환됩니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
