package com.fuelstation.managmentapi.manager.application.query;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;

@Component
@RequiredArgsConstructor
public class ListManagerFuelStationsQuery {

    private final GetManagerByIdQuery getManagerByIdQuery;
    private final FuelStationRepository fuelStationRepository;

    public List<FuelStation> process(long managerId) {
        getManagerByIdQuery.process(managerId);
        return fuelStationRepository.findFuelStationsByManagerId(managerId);
    }

}
