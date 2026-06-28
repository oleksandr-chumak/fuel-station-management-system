package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;
import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationAccessDeniedException;
import com.fuelstation.managmentapi.fuelstation.application.query.GetActiveFuelStationByIdQuery;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelTankHasPendingFuelOrdersException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DecommissionFuelTank {

    private final GetActiveFuelStationByIdQuery getActiveFuelStationByIdQuery;
    private final FuelStationRepository fuelStationRepository;
    private final FuelOrderRepository fuelOrderRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelStation process(long fuelStationId, long fuelTankId, Actor performedBy) {
        var fuelStation = getActiveFuelStationByIdQuery.process(fuelStationId, performedBy);
        checkAccess(fuelStation, performedBy);

        var pendingOrderIds = fuelOrderRepository.findPendingByFuelTankId(fuelTankId).stream()
            .map(FuelOrder::getFuelOrderId)
            .toList();
        if (!pendingOrderIds.isEmpty()) {
            throw new FuelTankHasPendingFuelOrdersException(fuelStationId, fuelTankId, pendingOrderIds);
        }

        fuelStation.decommissionFuelTank(fuelTankId, performedBy);

        FuelStation saved = fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return saved;
    }

    private void checkAccess(FuelStation fuelStation, Actor actor) {
        var hasAccess = actor.isSystem() ||
            actor.isAdmin() ||
            (actor.isManager() && fuelStation.isManagerAssigned(actor.id()));

        if (!hasAccess) {
            throw new FuelStationAccessDeniedException(fuelStation.getFuelStationId(), actor);
        }
    }
}
