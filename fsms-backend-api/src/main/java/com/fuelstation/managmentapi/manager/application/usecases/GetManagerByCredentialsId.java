package com.fuelstation.managmentapi.manager.application.usecases;

import lombok.AllArgsConstructor;

import com.fuelstation.managmentapi.manager.application.exceptions.ManagerNotFoundException;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetManagerByCredentialsId {
    
    private final ManagerRepository managerRepository;

    public Manager process(long managerId) {
        return managerRepository.findById(managerId)
            .orElseThrow(() -> new ManagerNotFoundException(managerId));
    }
}
