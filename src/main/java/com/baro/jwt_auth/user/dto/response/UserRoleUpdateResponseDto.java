package com.baro.jwt_auth.user.dto.response;

import com.baro.jwt_auth.user.entity.UserEntity;
import com.baro.jwt_auth.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRoleUpdateResponseDto {
    private User user;

    public static UserRoleUpdateResponseDto of(UserEntity userEntity) {
        return UserRoleUpdateResponseDto.builder()
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
