package com.baro.jwt_auth.user.entity;

import com.baro.jwt_auth.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public static UserEntity createUser(String username, String password, String nickname) {
        return UserEntity.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .role(UserRoleEnum.USER)
                .build();
    }

    public void updateAdminRole() {
        this.role = UserRoleEnum.ADMIN;
    }
}
