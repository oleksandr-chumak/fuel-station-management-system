package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationEvent;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateFuelStationEvent {

    private final FuelStationEventRepository repository;

    public FuelStationEvent process(FuelStationEvent event) {
        return repository.save(event);
    }

}
