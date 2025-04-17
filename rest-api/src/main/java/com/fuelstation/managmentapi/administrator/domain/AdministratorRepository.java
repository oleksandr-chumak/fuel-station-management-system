package com.fuelstation.managmentapi.administrator.domain;

import java.util.Optional;

public interface AdministratorRepository {
    public Administrator save(Administrator administrator);
    public Optional<Administrator> findById(Long id);
    public Optional<Administrator> findByEmail(String email);
}
