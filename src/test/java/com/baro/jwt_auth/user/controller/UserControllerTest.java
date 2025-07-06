package com.baro.jwt_auth.user.controller;

import com.baro.jwt_auth.common.exception.CustomException;
import com.baro.jwt_auth.common.response.ErrorCode;
import com.baro.jwt_auth.config.SecurityConfig;
import com.baro.jwt_auth.user.dto.request.LoginRequestDto;
import com.baro.jwt_auth.user.dto.request.SignupRequestDto;
import com.baro.jwt_auth.user.dto.response.LoginResponseDto;
import com.baro.jwt_auth.user.dto.response.SignupResponseDto;
import com.baro.jwt_auth.user.dto.response.UserRoleUpdateResponseDto;
import com.baro.jwt_auth.user.entity.UserEntity;
import com.baro.jwt_auth.user.entity.UserRoleEnum;
import com.baro.jwt_auth.security.jwt.JwtUtil;
import com.baro.jwt_auth.security.UserDetailsServiceImpl;
import com.baro.jwt_auth.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    // ========== 회원가입 테스트 ==========

    // 회원가입 성공
    @Test
    @DisplayName("회원가입 성공")
    void testSignUpSuccess() throws Exception {
        // given
        Long userId = 1L;
        String username = "testuser";
        String password = "Test1234!@";
        String nickname = "testuser";
        UserRoleEnum role = UserRoleEnum.ADMIN;

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .username(username)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build();

        SignupRequestDto request = new SignupRequestDto(username, password, nickname);

        given(userService.signup(any())).willReturn(SignupResponseDto.of(userEntity));

        // when & then
        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // 회원가입 실패 - 아이디 중복
    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void testSignUpConflict() throws Exception {
        // given
        SignupRequestDto request = new SignupRequestDto("testuser", "Test1234!@", "testuser");

        given(userService.signup(any())).willThrow(new CustomException(ErrorCode.USER_ALREADY_EXISTS));

        // when & then
        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }



    // ========== 로그인 테스트 ==========

    // 로그인 성공
    @Test
    @DisplayName("로그인 성공")
    void testSignInSuccess() throws Exception {
        // given
        String username = "testuser";
        String password = "Test1234!@";
        String nickname = "testnickname";

        LoginRequestDto request = new LoginRequestDto(username, password);

        // 서비스 mock이 리턴할 가짜 응답
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username(username)
                .password(password)
                .nickname(nickname)
                .role(UserRoleEnum.USER)
                .build();

        String accessToken = "mock-access-token";

        LoginResponseDto response = LoginResponseDto.of(userEntity, accessToken);

        given(userService.login(any())).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // 로그인 - 아이디 불일치
    @Test
    @DisplayName("로그인 실패 - 아이디 불일치")
    void testSignInUserNotFound() throws Exception {
        // given
        String username = "wronguser";
        String password = "Test1234!@";

        LoginRequestDto request = new LoginRequestDto(username, password);

        given(userService.login(any()))
                .willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        // when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // 로그인 - 비밀번호 불일치
    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void testSignInPasswordMismatch() throws Exception {
        // given
        String username = "testuser";
        String password = "wrongpass";

        LoginRequestDto request = new LoginRequestDto(username, password);

        given(userService.login(any()))
                .willThrow(new CustomException(ErrorCode.INVALID_CREDENTIALS));

        // when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }



    // ========== 관리자 권한 부여 테스트 ==========

    // 관리자 권한 부여 - 성공
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("관리자 권한 부여 - 성공")
    void testGrantAdminRoleSuccess() throws Exception {
        Long targetUserId = 15L;

        UserEntity userEntity = UserEntity.builder()
                .id(targetUserId)
                .username("testuser")
                .nickname("testnickname")
                .password("encoded")
                .role(UserRoleEnum.ADMIN)
                .build();

        UserRoleUpdateResponseDto responseDto = UserRoleUpdateResponseDto.of(userEntity);

        given(userService.grantAdminRole(any(), any())).willReturn(responseDto);

        // when & then
        mockMvc.perform(patch("/api/admin/users/{userId}/roles", targetUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // 권한 없는 사용자 시도
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("관리자 권한 부여 실패 - 일반 사용자 접근")
    void testGrantAdminRoleForbidden() throws Exception {
        Long targetUserId = 15L;

        mockMvc.perform(patch("/api/admin/users/{userId}/roles", targetUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // 존재하지 않는 사용자
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("관리자 권한 부여 실패 - 존재하지 않는 사용자")
    void testGrantAdminRoleUserNotFound() throws Exception {
        Long targetUserId = 999L;

        given(userService.grantAdminRole(any(), any()))
                .willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(patch("/api/admin/users/{userId}/roles", targetUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
