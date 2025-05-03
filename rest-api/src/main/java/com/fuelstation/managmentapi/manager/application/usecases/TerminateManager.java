package com.fuelstation.managmentapi.manager.application.usecases;

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
    
    @Autowired
    private GetManagerById getManagerById;
    
    public Manager process(long managerId) {
        Manager manger = getManagerById.process(managerId);
        manger.terminate();
        managerRepository.save(manger);
        domainEventPublisher.publishAll(manger.getDomainEvents());
        return manger;
    }
}