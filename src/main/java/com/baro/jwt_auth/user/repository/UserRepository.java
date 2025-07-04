package com.baro.jwt_auth.user.repository;

import com.baro.jwt_auth.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
