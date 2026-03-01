package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.manager.domain.Manager;

@Repository
@AllArgsConstructor
public class ManagerRepositoryImpl implements ManagerRepository {

    private JpaUserRepository jpaUserRepository;
    private ManagerMapper managerMapper;

    @Override
    public Manager save(Manager manager) {
        return managerMapper.toDomain(jpaUserRepository.save(managerMapper.toEntity(manager)));
    }

    @Override
    public List<Manager> findAll() {
        return jpaUserRepository.findByUserRoleId(UserRole.MANAGER.getId()).stream().map(managerMapper::toDomain).toList();
    }

    @Override
    public List<Manager> findByIds(List<Long> managerIds) {
        return jpaUserRepository.findAllById(managerIds).stream().map(managerMapper::toDomain).toList();
    }

    @Override
    public Optional<Manager> findById(long id) {
        return jpaUserRepository.findByUserIdAndUserRoleId(id, UserRole.MANAGER.getId()).map(managerMapper::toDomain);
    }

}
