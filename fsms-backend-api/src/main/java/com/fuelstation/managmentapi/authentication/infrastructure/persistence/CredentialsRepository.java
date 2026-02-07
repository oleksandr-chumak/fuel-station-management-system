package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import java.util.Optional;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

public interface CredentialsRepository {
     Credentials save(Credentials credentials);
     Optional<Credentials> findById(Long id);
     Optional<Credentials> findByEmailAndRole(String email, UserRole role);
     Optional<Credentials> findByEmail(String email);
     Optional<Credentials> findByUsername(String username);
}
