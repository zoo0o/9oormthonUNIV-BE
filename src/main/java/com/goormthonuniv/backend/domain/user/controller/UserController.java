package com.goormthonuniv.backend.domain.user.controller;

import com.goormthonuniv.backend.domain.user.dto.SignupRequest;
import com.goormthonuniv.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "회원 관련 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 닉네임으로 회원가입을 수행합니다.")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        Long userId = userService.createUser(request);
        return ResponseEntity.ok(userId);
    }
}