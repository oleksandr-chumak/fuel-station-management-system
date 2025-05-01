package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import java.util.Optional;

import com.fuelstation.managmentapi.administrator.domain.Administrator;

public interface AdministratorRepository {
    public Administrator save(Administrator administrator);
    public Optional<Administrator> findById(Long id);
    public Optional<Administrator> findByEmail(String email);
}
