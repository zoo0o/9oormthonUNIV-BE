package com.goormthonuniv.backend.domain.user.controller;

import com.goormthonuniv.backend.domain.user.dto.UserSignupRequest;
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
@Tag(name = "회원", description = "회원 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 닉네임으로 회원가입을 수행합니다.")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UserSignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok("회원가입 완료!");
    }
}
