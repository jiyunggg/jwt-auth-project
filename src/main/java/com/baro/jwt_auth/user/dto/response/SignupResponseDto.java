package com.baro.jwt_auth.user.dto.response;

import com.baro.jwt_auth.user.entity.UserEntity;
import com.baro.jwt_auth.user.entity.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponseDto {
    @Schema(description = "회원가입된 사용자 정보")
    private User user;

    public static SignupResponseDto of(UserEntity userEntity) {
        return SignupResponseDto.builder()
                .user(User.from(userEntity))
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

        public static User from(UserEntity userEntity) {
            return User.builder()
                    .username(userEntity.getUsername())
                    .nickname(userEntity.getNickname())
                    .role(userEntity.getRole())
                    .build();
        }
    }
}
