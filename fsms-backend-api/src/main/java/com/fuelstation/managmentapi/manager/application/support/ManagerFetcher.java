package com.fuelstation.managmentapi.manager.application.support;

import com.fuelstation.managmentapi.manager.application.exceptions.ManagerNotFoundException;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ManagerFetcher {

    private final ManagerRepository managerRepository;

    public Manager fetchById(long managerId) {
        return managerRepository.findByCredentialsId(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(managerId));
    }
}
