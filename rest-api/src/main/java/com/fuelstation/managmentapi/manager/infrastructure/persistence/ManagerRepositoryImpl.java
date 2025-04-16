package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.domain.ManagerRepository;

@Repository
public class ManagerRepositoryImpl implements ManagerRepository {

    @Override
    public Manager save(Manager manager) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Optional<Manager> findById(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
    
}
