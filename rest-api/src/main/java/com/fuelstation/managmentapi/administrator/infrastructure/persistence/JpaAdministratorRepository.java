package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface JpaAdministratorRepository extends JpaRepository<AdministratorEntity, Long> {

    Optional<AdministratorEntity> findByCredentialsId(long credentialsId);

}

