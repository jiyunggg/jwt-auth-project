package com.baro.jwt_auth.user.dto.response;

import com.baro.jwt_auth.user.entity.UserEntity;
import com.baro.jwt_auth.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponseDto {
    private User user;

    public static SignupResponseDto of(UserEntity userEntity) {
        return SignupResponseDto.builder()
                .user(User.from(userEntity))
                .build();
    }

    @Getter
    @Builder
    public static class User {
        private String username;
        private String nickname;
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
