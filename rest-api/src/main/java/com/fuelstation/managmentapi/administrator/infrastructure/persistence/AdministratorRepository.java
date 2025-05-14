package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import java.util.Optional;

import com.fuelstation.managmentapi.administrator.domain.Administrator;

public interface AdministratorRepository {
    public Administrator save(Administrator administrator);
    public Optional<Administrator> findById(long id);
    public Optional<Administrator> findByCredentialsId(long id);
}
