package com.baro.jwt_auth.user.dto.response;

import com.baro.jwt_auth.user.entity.UserEntity;
import com.baro.jwt_auth.user.entity.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    @Schema(description = "로그인한 사용자 정보")
    private User user;

    public static LoginResponseDto of(UserEntity userEntity, String accessToken) {
        return LoginResponseDto.builder()
                .user(User.from(userEntity, accessToken))
                .build();
    }

    @Getter
    @Builder
    public static class User {
        @Schema(description = "사용자 ID", example = "aaa")
        private String username;

        @Schema(description = "사용자 닉네임", example = "aaa")
        private String nickname;

        @Schema(description = "사용자 권한", example = "USER")
        private UserRoleEnum role;

        @Schema(description = "JWT 액세스 토큰", example = "Bearer eyI1NiJ9..")
        private String accessToken;

        public static User from(UserEntity userEntity, String accessToken) {
            return User.builder()
                    .username(userEntity.getUsername())
                    .nickname(userEntity.getNickname())
                    .role(userEntity.getRole())
                    .accessToken(accessToken)
                    .build();
        }
    }
}
