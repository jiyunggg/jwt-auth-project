package com.baro.jwt_auth.user.service;

import com.baro.jwt_auth.common.exception.CustomException;
import com.baro.jwt_auth.common.response.ErrorCode;
import com.baro.jwt_auth.user.dto.request.LoginRequestDto;
import com.baro.jwt_auth.user.dto.request.SignupRequestDto;
import com.baro.jwt_auth.user.dto.response.LoginResponseDto;
import com.baro.jwt_auth.user.dto.response.SignupResponseDto;
import com.baro.jwt_auth.user.dto.response.UserRoleUpdateResponseDto;
import com.baro.jwt_auth.user.entity.UserEntity;
import com.baro.jwt_auth.user.entity.UserRoleEnum;
import com.baro.jwt_auth.user.jwt.JwtUtil;
import com.baro.jwt_auth.user.repository.UserRepository;
import com.baro.jwt_auth.user.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
                .role(UserRoleEnum.USER)
                .build();

        userRepository.save(user);
        return SignupResponseDto.of(user);
    }

    // 로그인
    public LoginResponseDto login(LoginRequestDto reqDto) {
        String reqUsername = reqDto.getUsername();
        String reqPassword = reqDto.getPassword();

        // 유저 조회
        UserEntity user = userRepository.findByUsernameAndIsDeletedFalse(reqUsername)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 체크
        if (!passwordEncoder.matches(reqPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // JWT 토큰 생성
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());

        return LoginResponseDto.of(user, token);
    }

    // 관리자 권한 부여
    @Transactional
    public UserRoleUpdateResponseDto grantAdminRole(Long userId, UserDetailsImpl loggedInUser) {
        // 관리자 권한 체크
        if (!loggedInUser.hasRole(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        UserEntity user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateAdminRole();

        return UserRoleUpdateResponseDto.of(user);
    }
}
