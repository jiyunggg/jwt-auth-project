package com.baro.jwt_auth.user.controller;

import com.baro.jwt_auth.user.dto.request.LoginRequestDto;
import com.baro.jwt_auth.user.dto.request.SignupRequestDto;
import com.baro.jwt_auth.user.dto.response.LoginResponseDto;
import com.baro.jwt_auth.user.dto.response.SignupResponseDto;
import com.baro.jwt_auth.user.dto.response.UserRoleUpdateResponseDto;
import com.baro.jwt_auth.user.security.UserDetailsImpl;
import com.baro.jwt_auth.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
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

    // 관리자 권한 부여
    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<UserRoleUpdateResponseDto> grantAdminRole(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl loggedInUser) {
        UserRoleUpdateResponseDto resDto = userService.grantAdminRole(userId, loggedInUser);
        return ResponseEntity.ok(resDto);
    }

}
