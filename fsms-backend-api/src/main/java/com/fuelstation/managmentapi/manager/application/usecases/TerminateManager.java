package com.fuelstation.managmentapi.manager.application.usecases;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TerminateManager {
    
    private final ManagerRepository managerRepository;

    private final DomainEventPublisher domainEventPublisher;
    
    private final GetManagerById getManagerById;

    public TerminateManager(ManagerRepository managerRepository, DomainEventPublisher domainEventPublisher, GetManagerById getManagerById) {
        this.managerRepository = managerRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.getManagerById = getManagerById;
    }

    @Transactional
    public Manager process(long managerId) {
        Manager manger = getManagerById.process(managerId);

        manger.terminate();

        managerRepository.save(manger);
        domainEventPublisher.publishAll(manger.getDomainEvents());

        return manger;
    }

}