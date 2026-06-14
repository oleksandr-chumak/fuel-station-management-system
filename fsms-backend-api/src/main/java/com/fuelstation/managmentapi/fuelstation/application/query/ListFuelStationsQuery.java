package com.fuelstation.managmentapi.fuelstation.application.query;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;

@Component
@AllArgsConstructor
public class ListFuelStationsQuery {

    private final FuelStationRepository fuelStationRepository;

    public List<FuelStation> process() {
        return fuelStationRepository.findAll();
    }

}
