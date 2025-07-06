package com.baro.jwt_auth.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @Schema(description = "사용자 ID", example = "admin")
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @Schema(description = "사용자 비밀번호", example = "admin1234")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
