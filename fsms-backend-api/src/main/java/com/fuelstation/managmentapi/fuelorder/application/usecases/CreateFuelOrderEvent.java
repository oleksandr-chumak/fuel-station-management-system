package com.fuelstation.managmentapi.fuelorder.application.usecases;

import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderEvent;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateFuelOrderEvent {

    private final FuelOrderEventRepository repository;

    public FuelOrderEvent process(FuelOrderEvent event) {
        return repository.save(event);
    }

}
