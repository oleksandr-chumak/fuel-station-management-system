package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationFactory;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationCreated;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;

@Component
@AllArgsConstructor
public class CreateFuelStation {

    private final FuelStationRepository fuelStationRepository;
    private final FuelStationFactory fuelStationFactory;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelStation process(
            String street,
            String buildingNumber,
            String city,
            String postalCode,
            String country,
            Actor performedBy
    ) {
        var fuelStation = fuelStationFactory.create(street, buildingNumber, city, postalCode, country);

        var savedFuelStation = fuelStationRepository.save(fuelStation);
        domainEventPublisher.publish(new FuelStationCreated(savedFuelStation.getFuelStationId(), performedBy));

        return savedFuelStation;
    } 
    
}
