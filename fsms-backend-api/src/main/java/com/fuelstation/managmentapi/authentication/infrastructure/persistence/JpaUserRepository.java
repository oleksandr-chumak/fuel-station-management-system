package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByUserRoleId(Long userRoleId);
    Optional<UserEntity> findByEmailAndUserRoleId(String email, Long userRoleId);
    Optional<UserEntity> findByUserIdAndUserRoleId(Long userId, Long userRoleId);
}
