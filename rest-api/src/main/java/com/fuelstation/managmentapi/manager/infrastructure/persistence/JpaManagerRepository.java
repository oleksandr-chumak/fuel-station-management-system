package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface JpaManagerRepository extends JpaRepository<ManagerEntity, Long> {
    Optional<ManagerEntity> findByCredentialsId(long credentialsId);
}
