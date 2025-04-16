package com.fuelstation.managmentapi.authentication.domain;

import java.util.Optional;

public interface CredentialsRepository {
    public Credentials save(Credentials credentials);
    public Optional<Credentials> findById(Long id);    
}
