package com.baro.jwt_auth.user.config;

import com.baro.jwt_auth.user.entity.UserEntity;
import com.baro.jwt_auth.user.entity.UserRoleEnum;
import com.baro.jwt_auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminUsername = "admin";

        // 이미 관리자 계정이 존재할 경우
        if (userRepository.existsByUsernameAndIsDeletedFalse(adminUsername)) {
            return;
        }

        // 비밀번호 해싱
        String rawPassword = "admin1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 관리자 계정 생성
        UserEntity adminUser = UserEntity.builder()
                .username(adminUsername)
                .password(encodedPassword)
                .nickname("관리자")
                .role(UserRoleEnum.ADMIN)
                .build();

        userRepository.save(adminUser);

        System.out.println("[AdminDataLoader] 관리자 계정 생성 완료");
        System.out.println("[AdminDataLoader] username: " + adminUsername + " / password: " + rawPassword);
    }
}
