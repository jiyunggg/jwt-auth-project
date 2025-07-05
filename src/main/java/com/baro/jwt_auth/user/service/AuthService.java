package com.baro.jwt_auth.user.service;

import com.baro.jwt_auth.common.exception.CustomException;
import com.baro.jwt_auth.common.response.ErrorCode;
import com.baro.jwt_auth.user.dto.request.SignupRequestDto;
import com.baro.jwt_auth.user.dto.response.SignupResponseDto;
import com.baro.jwt_auth.user.entity.UserEntity;
import com.baro.jwt_auth.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public SignupResponseDto signup(SignupRequestDto reqDto) {
        String username = reqDto.getUser().getUsername();
        String password = passwordEncoder.encode(reqDto.getUser().getPassword()); // 비밀번호 암호화

        // ID 중복 체크
        if(userRepository.existsByUsernameAndIsDeletedFalse(username)) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(password)
                .nickname(reqDto.getUser().getNickname())
                .role(reqDto.getUser().getRole())
                .build();

        userRepository.save(user);
        return SignupResponseDto.of(user);
    }
}
