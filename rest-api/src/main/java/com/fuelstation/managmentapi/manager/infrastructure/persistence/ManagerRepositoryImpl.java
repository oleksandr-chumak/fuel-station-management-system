package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.domain.ManagerRepository;

@Repository
public class ManagerRepositoryImpl implements ManagerRepository {

    @Autowired
    private JpaManagerRepository jpaManagerRepository;

    @Autowired
    private ManagerMapper managerMapper;
    
    @Override
    public Manager save(Manager manager) {
        ManagerEntity managerEntity = jpaManagerRepository.save(managerMapper.toEntity(manager));
        return managerMapper.toDomain(managerEntity);
    }

    @Override
    public Optional<Manager> findById(long id) {
        return jpaManagerRepository.findById(id).map(managerMapper::toDomain);  
    }

    @Override
    public List<Manager> findAll() {
        return jpaManagerRepository.findAll().stream().map(managerMapper::toDomain).toList();
    }

    @Override
    public List<Manager> findManagersAssignedToFuelStation(Long fuelStationId) {
        return jpaManagerRepository.findManagersByFuelStationId(fuelStationId).stream().map(managerMapper::toDomain).toList();
    }
    
}
