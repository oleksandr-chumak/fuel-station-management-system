package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.manager.domain.Manager;

public interface ManagerRepository {
   Manager save(Manager manager);
   Optional<Manager> findById(long id);
   List<Manager> findAll();
   List<Manager> findByIds(List<Long> managerIds);
}
