package com.example.app.infrastructure.adapter.persistence.repository;

import com.example.app.infrastructure.adapter.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
