package com.baro.jwt_auth.user.repository;

import com.baro.jwt_auth.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsernameAndIsDeletedFalse(String username);
    Optional<UserEntity> findByUsernameAndIsDeletedFalse(String username);
}
