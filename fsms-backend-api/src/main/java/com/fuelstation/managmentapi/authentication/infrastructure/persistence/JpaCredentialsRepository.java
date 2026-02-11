package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCredentialsRepository extends JpaRepository<CredentialsEntity, Long> {
    Optional<CredentialsEntity> findByEmailAndUserRoleId(String email, Long userRoleId);
    Optional<CredentialsEntity> findByEmail(String email);
    Optional<CredentialsEntity> findByUsername(String username);
}
