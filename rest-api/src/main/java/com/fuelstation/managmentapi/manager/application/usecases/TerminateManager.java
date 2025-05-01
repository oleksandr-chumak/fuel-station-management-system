package com.fuelstation.managmentapi.manager.application.usecases;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
public class TerminateManager {
    
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;
    
    public Manager process(Long managerId) {
        Manager manger = managerRepository.findById(managerId)
            .orElseThrow(() -> new NoSuchElementException("Manager with id:" + managerId + "doesn't exist"));
        manger.terminate();
        managerRepository.save(manger);
        domainEventPublisher.publishAll(manger.getDomainEvents());
        return manger;
    }
}