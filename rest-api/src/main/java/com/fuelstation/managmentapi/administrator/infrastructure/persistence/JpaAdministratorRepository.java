package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAdministratorRepository extends JpaRepository<AdministratorEntity, Long> {
    AdministratorEntity findByCredentialsId(Long credentialsId);
}

