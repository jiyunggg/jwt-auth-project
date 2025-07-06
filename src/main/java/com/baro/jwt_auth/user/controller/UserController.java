package com.baro.jwt_auth.user.controller;

import com.baro.jwt_auth.common.response.ResErrorDTO;
import com.baro.jwt_auth.user.dto.request.LoginRequestDto;
import com.baro.jwt_auth.user.dto.request.SignupRequestDto;
import com.baro.jwt_auth.user.dto.response.LoginResponseDto;
import com.baro.jwt_auth.user.dto.response.SignupResponseDto;
import com.baro.jwt_auth.user.dto.response.UserRoleUpdateResponseDto;
import com.baro.jwt_auth.security.UserDetailsImpl;
import com.baro.jwt_auth.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "회원가입",
            description = "신규 사용자를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = SignupResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "아이디 중복",
                    content = @Content(schema = @Schema(implementation = ResErrorDTO.class))
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto reqDto) {
        SignupResponseDto resDto = userService.signup(reqDto);
        return ResponseEntity.ok(resDto);
    }

    // 로그인
    @Operation(
            summary = "로그인",
            description = "사용자 로그인 및 JWT 토큰 발급"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ResErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 회원",
                    content = @Content(schema = @Schema(implementation = ResErrorDTO.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto reqDto) {
        LoginResponseDto resDto = userService.login(reqDto);
        return ResponseEntity.ok(resDto);
    }

    // 관리자 권한 부여
    @Operation(
            summary = "관리자 권한 부여",
            description = "지정한 사용자 ID에 관리자 역할을 부여합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "권한 부여 성공",
                    content = @Content(schema = @Schema(implementation = UserRoleUpdateResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 인증 토큰",
                    content = @Content(schema = @Schema(implementation = ResErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ResErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ResErrorDTO.class))
            )
    })
    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<UserRoleUpdateResponseDto> grantAdminRole(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl loggedInUser) {
        UserRoleUpdateResponseDto resDto = userService.grantAdminRole(userId, loggedInUser);
        return ResponseEntity.ok(resDto);
    }

}
