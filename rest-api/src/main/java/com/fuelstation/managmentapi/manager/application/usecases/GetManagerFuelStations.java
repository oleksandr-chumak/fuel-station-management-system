package com.fuelstation.managmentapi.manager.application.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class GetManagerFuelStations {
    
    @Autowired
    private GetManagerById getManagerById;

    @Autowired
    private FuelStationRepository fuelStationRepository;


    public List<FuelStation> process(long managerId) {
        getManagerById.process(managerId);
        return fuelStationRepository.findFuelStationsByManagerId(managerId);
    }
    
}
