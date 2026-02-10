package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.manager.domain.Manager;

@Repository
@AllArgsConstructor
public class ManagerRepositoryImpl implements ManagerRepository {

    private JpaManagerRepository jpaManagerRepository;
    private ManagerMapper managerMapper;
    
    @Override
    public Manager save(Manager manager) {
        var test = managerMapper.toEntity(manager);
        ManagerEntity managerEntity = jpaManagerRepository.save(test);
        return managerMapper.toDomain(managerEntity);
    }

    @Override
    public List<Manager> findAll() {
        return jpaManagerRepository.findAll().stream().map(managerMapper::toDomain).toList();
    }

    @Override
    public List<Manager> findManagersByIds(List<Long> assignedManagerIds) {
        return jpaManagerRepository.findAllById(assignedManagerIds).stream().map(managerMapper::toDomain).toList();
    }

    @Override
    public Optional<Manager> findByCredentialsId(long id) {
        return jpaManagerRepository.findByCredentialsId(id).map(managerMapper::toDomain);
    }

}
