package com.baro.jwt_auth.user.dto.response;

import com.baro.jwt_auth.user.entity.UserEntity;
import com.baro.jwt_auth.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private User user;

    public static LoginResponseDto of(UserEntity userEntity, String accessToken) {
        return LoginResponseDto.builder()
                .user(User.from(userEntity, accessToken))
                .build();
    }

    @Getter
    @Builder
    public static class User {
        private String username;
        private String nickname;
        private UserRoleEnum role;
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
