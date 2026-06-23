package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.application.query.GetActiveFuelStationByIdQuery;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class DispenseFuel {

    private final GetActiveFuelStationByIdQuery getActiveFuelStationByIdQuery;
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelStation process(long fuelStationId, long fuelTankId, BigDecimal volume, Actor performedBy) {
        var fuelStation = getActiveFuelStationByIdQuery.process(fuelStationId, performedBy);

        fuelStation.dispenseFuel(fuelTankId, volume, performedBy);

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }
}
