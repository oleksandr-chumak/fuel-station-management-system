package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.manager.domain.Manager;

public interface ManagerRepository {
   Manager save(Manager manager);
   Optional<Manager> findByCredentialsId(long id);
   List<Manager> findAll();
   List<Manager> findManagersByIds(List<Long> assignedManagerIds);
}
