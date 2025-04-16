package com.fuelstation.managmentapi.manager.domain;

import java.util.Optional;

public interface ManagerRepository {
   public Manager save(Manager manager);
   public Optional<Manager> findById(long id);   
}
