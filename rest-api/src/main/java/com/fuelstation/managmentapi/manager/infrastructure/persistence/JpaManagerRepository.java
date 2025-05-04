package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaManagerRepository extends JpaRepository<ManagerEntity, Long> {}
