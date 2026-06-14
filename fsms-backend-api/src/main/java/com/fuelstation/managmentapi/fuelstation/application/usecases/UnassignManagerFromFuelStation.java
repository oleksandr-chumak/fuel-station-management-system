package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationAccessDeniedException;
import com.fuelstation.managmentapi.fuelstation.application.query.GetActiveFuelStationByIdQuery;
import com.fuelstation.managmentapi.manager.application.support.ManagerFetcher;
import com.fuelstation.managmentapi.manager.domain.Manager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class UnassignManagerFromFuelStation {

    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final ManagerFetcher managerFetcher;
    private final GetActiveFuelStationByIdQuery getActiveFuelStationByIdQuery;

    @Transactional
    public Manager process(long fuelStationId, long managerId, Actor performedBy) {
        checkAccess(fuelStationId, performedBy);
        var fuelStation = getActiveFuelStationByIdQuery.process(fuelStationId, performedBy);
        var manager = managerFetcher.fetchById(managerId);

        fuelStation.unassignManager(manager.getManagerId(), performedBy);

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return manager;
    }

    private void checkAccess(long fuelStationId, Actor actor) {
        var hasAccess = actor.isSystem() || actor.isAdmin();
        if (!hasAccess) {
            throw new FuelStationAccessDeniedException(fuelStationId, actor);
        }
    }
}
