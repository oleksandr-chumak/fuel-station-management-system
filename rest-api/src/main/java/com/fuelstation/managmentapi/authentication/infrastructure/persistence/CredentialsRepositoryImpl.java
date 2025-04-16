package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.CredentialsRepository;

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
}