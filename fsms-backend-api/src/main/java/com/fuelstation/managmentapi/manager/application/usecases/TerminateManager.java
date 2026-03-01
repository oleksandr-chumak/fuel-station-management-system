package com.fuelstation.managmentapi.manager.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class TerminateManager {
    
    private final ManagerRepository managerRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final GetManagerByCredentialsId getManagerByCredentialsId;

    @Transactional
    public Manager process(long managerId, Actor performedBy) {
        Manager manger = getManagerByCredentialsId.process(managerId);

        manger.terminate(performedBy);

        managerRepository.save(manger);
        domainEventPublisher.publishAll(manger.getDomainEvents());

        return manger;
    }

}