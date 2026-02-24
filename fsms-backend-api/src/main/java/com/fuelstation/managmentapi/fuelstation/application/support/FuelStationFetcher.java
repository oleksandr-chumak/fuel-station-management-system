package com.fuelstation.managmentapi.fuelstation.application.support;

import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationDeactivatedException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FuelStationFetcher {

    private final FuelStationRepository repository;

    public FuelStation fetchById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new FuelStationNotFoundException(id));
    }

    public FuelStation fetchActiveById(long id) {
        FuelStation station = fetchById(id);
        if (station.deactivated()) {
            throw new FuelStationDeactivatedException(id);
        }
        return station;
    }
}
