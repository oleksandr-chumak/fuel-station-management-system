package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCredentialsRepository extends JpaRepository<CredentialsEntity, Long> {
    CredentialsEntity findByEmail(String email);
}
