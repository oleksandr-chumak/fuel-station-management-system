package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Repository
public class CredentialsRepositoryImpl implements CredentialsRepository {

    private final JpaCredentialsRepository jpaCredentialsRepository;

    public CredentialsRepositoryImpl(JpaCredentialsRepository jpaCredentialsRepository) {
        this.jpaCredentialsRepository = jpaCredentialsRepository;
    }

    @Override
    public Credentials save(Credentials credentials) {
        CredentialsEntity entity = CredentialsMapper.toEntity(credentials);
        entity = jpaCredentialsRepository.save(entity);
        return CredentialsMapper.toDomain(entity);
    }

    @Override
    public Optional<Credentials> findById(Long id) {
        return jpaCredentialsRepository.findById(id)
                .map(CredentialsMapper::toDomain);
    }

    @Override
    public Optional<Credentials> findByEmailAndRole(String email, UserRole role) {
        return jpaCredentialsRepository.findByEmailAndRole(email, role).map(CredentialsMapper::toDomain);
    }

    @Override
    public Optional<Credentials> findByEmail(String email) {
        return jpaCredentialsRepository.findByEmail(email).map(CredentialsMapper::toDomain);
    }
}