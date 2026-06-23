package com.fuelstation.managmentapi.fuelstation.application.query;

import com.fuelstation.managmentapi.fuelstation.application.rest.response.FuelTankVolumeHistoryResponse;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelTankVolumeHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListFuelTankVolumeHistoryQuery {

    private final FuelTankVolumeHistoryRepository repository;

    public List<FuelTankVolumeHistoryResponse> process(long fuelStationId) {
        return repository.findByFuelStationId(fuelStationId).stream()
                .map(entry -> new FuelTankVolumeHistoryResponse(
                        entry.fuelTankId(),
                        entry.fuelGrade().toString(),
                        entry.oldVolume(),
                        entry.newVolume(),
                        entry.reason().name(),
                        entry.changedAt(),
                        entry.performedBy().isSystem() ? null : entry.performedBy().id()
                ))
                .toList();
    }

}
