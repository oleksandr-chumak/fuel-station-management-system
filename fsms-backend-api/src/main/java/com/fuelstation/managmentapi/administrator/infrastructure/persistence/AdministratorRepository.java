package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import java.util.Optional;

import com.fuelstation.managmentapi.administrator.domain.Administrator;

public interface AdministratorRepository {
    Administrator save(Administrator administrator);
    Optional<Administrator> findByCredentialsId(long id);
}
