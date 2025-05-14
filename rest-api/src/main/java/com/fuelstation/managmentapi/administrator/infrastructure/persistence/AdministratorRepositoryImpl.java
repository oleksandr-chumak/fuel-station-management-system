package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.administrator.domain.Administrator;

@Repository
public class AdministratorRepositoryImpl implements AdministratorRepository {

    @Autowired
    private JpaAdministratorRepository jpaAdministratorRepository;

    @Autowired
    private AdministratorMapper administratorMapper;

    @Override
    public Administrator save(Administrator administrator) {
        AdministratorEntity entity = administratorMapper.toEntity(administrator);
        entity = jpaAdministratorRepository.save(entity);
        return administratorMapper.toDomain(entity);
    }

    @Override
    public Optional<Administrator> findById(long id) {
        return jpaAdministratorRepository.findById(id).map(administratorMapper::toDomain);
    }

    @Override
    public Optional<Administrator> findByCredentialsId(long id) {
        return jpaAdministratorRepository.findByCredentialsId(id).map(administratorMapper::toDomain);
    }

}