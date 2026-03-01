package com.fuelstation.managmentapi.manager.application.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;

@Component
public class GetManagerFuelStations {
    
    private final GetManagerByCredentialsId getManagerByCredentialsId;

    private final FuelStationRepository fuelStationRepository;

    public GetManagerFuelStations(GetManagerByCredentialsId getManagerByCredentialsId, FuelStationRepository fuelStationRepository) {
        this.getManagerByCredentialsId = getManagerByCredentialsId;
        this.fuelStationRepository = fuelStationRepository;
    }


    public List<FuelStation> process(long managerId) {
        getManagerByCredentialsId.process(managerId);
        return fuelStationRepository.findFuelStationsByManagerId(managerId);
    }
    
}
