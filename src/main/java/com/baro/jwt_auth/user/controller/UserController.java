package com.baro.jwt_auth.user.controller;

import com.baro.jwt_auth.user.dto.request.LoginRequestDto;
import com.baro.jwt_auth.user.dto.request.SignupRequestDto;
import com.baro.jwt_auth.user.dto.response.LoginResponseDto;
import com.baro.jwt_auth.user.dto.response.SignupResponseDto;
import com.baro.jwt_auth.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto reqDto) {
        SignupResponseDto resDto = userService.signup(reqDto);
        return ResponseEntity.ok(resDto);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto reqDto) {
        LoginResponseDto resDto = userService.login(reqDto);
        return ResponseEntity.ok(resDto);
    }
}
