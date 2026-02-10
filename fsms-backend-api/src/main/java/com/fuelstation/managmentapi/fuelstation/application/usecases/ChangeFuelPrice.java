package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
@AllArgsConstructor
public class ChangeFuelPrice {
   
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final GetFuelStationById getFuelStationById;

    @Transactional
    public FuelStation process(long fuelStationId, FuelGrade fuelGrade, float newPrice, Credentials credentials) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId, credentials);

        fuelStation.changeFuelPrice(fuelGrade, newPrice);

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }

}
