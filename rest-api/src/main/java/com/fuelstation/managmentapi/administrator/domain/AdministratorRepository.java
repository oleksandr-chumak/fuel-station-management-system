package com.fuelstation.managmentapi.administrator.domain;

import java.util.Optional;

public interface AdministratorRepository {
    // TODO i dont like it
    public Administrator create(Long credentialsId);
    public Administrator save(Administrator administrator);
    public Optional<Administrator> findById(Long id);
    public Optional<Administrator> findByEmail(String email);
}
