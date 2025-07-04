package com.fuelstation.managmentapi.manager.application.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class GetManagerFuelStations {
    
    private final GetManagerById getManagerById;

    private final FuelStationRepository fuelStationRepository;

    public GetManagerFuelStations(GetManagerById getManagerById, FuelStationRepository fuelStationRepository) {
        this.getManagerById = getManagerById;
        this.fuelStationRepository = fuelStationRepository;
    }


    public List<FuelStation> process(long managerId) {
        getManagerById.process(managerId);
        return fuelStationRepository.findFuelStationsByManagerId(managerId);
    }
    
}
