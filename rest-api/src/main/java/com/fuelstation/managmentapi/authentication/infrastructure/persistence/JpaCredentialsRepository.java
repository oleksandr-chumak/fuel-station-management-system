package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Repository
public interface JpaCredentialsRepository extends JpaRepository<CredentialsEntity, Long> {
    public Optional<CredentialsEntity> findByEmailAndRole(String email,  UserRole role);
    public Optional<CredentialsEntity> findByEmail(String email);
}
