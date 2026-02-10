package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.administrator.domain.Administrator;

@Repository
@AllArgsConstructor
public class AdministratorRepositoryImpl implements AdministratorRepository {

    private JpaAdministratorRepository jpaAdministratorRepository;
    private AdministratorMapper administratorMapper;

    @Override
    public Administrator save(Administrator administrator) {
        AdministratorEntity entity = administratorMapper.toEntity(administrator);
        entity = jpaAdministratorRepository.save(entity);
        return administratorMapper.toDomain(entity);
    }

    @Override
    public Optional<Administrator> findByCredentialsId(long id) {
        return jpaAdministratorRepository.findByCredentialsId(id).map(administratorMapper::toDomain);
    }

}