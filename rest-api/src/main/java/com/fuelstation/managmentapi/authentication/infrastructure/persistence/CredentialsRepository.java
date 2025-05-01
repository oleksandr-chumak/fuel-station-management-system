package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import java.util.Optional;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

public interface CredentialsRepository {
    public Credentials save(Credentials credentials);
    public Optional<Credentials> findById(Long id);    
    public Optional<Credentials> findByEmailAndRole(String email, UserRole role);
    public Optional<Credentials> findByEmail(String email);
}
