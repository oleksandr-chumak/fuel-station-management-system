package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import java.util.Optional;

import com.fuelstation.managmentapi.authentication.infrastructure.persistence.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.administrator.domain.Administrator;

@Repository
@AllArgsConstructor
public class AdministratorRepositoryImpl implements AdministratorRepository {

    private JpaUserRepository jpaUserRepository;
    private AdministratorMapper administratorMapper;

    @Override
    public Administrator save(Administrator administrator) {
        var entity = jpaUserRepository.save(administratorMapper.toEntity(administrator));
        return administratorMapper.toDomain(entity);
    }

    @Override
    public Optional<Administrator> findByCredentialsId(long id) {
        return jpaUserRepository.findById(id).map(administratorMapper::toDomain);
    }

}