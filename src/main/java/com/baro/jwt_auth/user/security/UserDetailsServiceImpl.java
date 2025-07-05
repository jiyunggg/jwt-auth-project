package com.baro.jwt_auth.user.security;

import com.baro.jwt_auth.common.exception.CustomException;
import com.baro.jwt_auth.common.response.ErrorCode;
import com.baro.jwt_auth.user.entity.UserEntity;
import com.baro.jwt_auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user);
    }
}
