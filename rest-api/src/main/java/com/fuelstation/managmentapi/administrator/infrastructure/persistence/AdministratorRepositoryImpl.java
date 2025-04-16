package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.administrator.domain.Administrator;
import com.fuelstation.managmentapi.administrator.domain.AdministratorRepository;

@Repository
public class AdministratorRepositoryImpl implements AdministratorRepository {

    private final JpaAdministratorRepository jpaAdministratorRepository;

    public AdministratorRepositoryImpl(JpaAdministratorRepository jpaAdministratorRepository) {
        this.jpaAdministratorRepository = jpaAdministratorRepository;
    }

    @Override
    public Administrator save(Administrator administrator) {
        AdministratorEntity entity = AdministratorMapper.toEntity(administrator);
        entity = jpaAdministratorRepository.save(entity);
        
        
        return AdministratorMapper.toDomain(entity);
    }

    @Override
    public Optional<Administrator> findById(Long id) {
        return jpaAdministratorRepository.findById(id).map(entity -> AdministratorMapper.toDomain(entity));
    }
}
    