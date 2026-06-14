package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.application.command.ChangeFuelPricesBulkCommand;
import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationAccessDeniedException;
import com.fuelstation.managmentapi.fuelstation.application.query.GetActiveFuelStationByIdQuery;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChangeFuelPricesBulk {

    private final GetActiveFuelStationByIdQuery getActiveFuelStationByIdQuery;
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelStation process(ChangeFuelPricesBulkCommand command) {
        checkAccess(command.fuelStationId(), command.performedBy());
        var fuelStation = getActiveFuelStationByIdQuery.process(command.fuelStationId(), command.performedBy());

        for (var change : command.changes()) {
            fuelStation.changeFuelPrice(change.fuelGrade(), change.newPrice(), command.performedBy());
        }

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }

    private void checkAccess(long fuelStationId, Actor actor) {
        var hasAccess = actor.isSystem() || actor.isAdmin();
        if(!hasAccess) {
            throw new FuelStationAccessDeniedException(fuelStationId, actor);
        }
    }
}
