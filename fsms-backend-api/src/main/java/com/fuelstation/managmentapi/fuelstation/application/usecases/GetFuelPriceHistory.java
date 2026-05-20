package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.fuelstation.application.rest.response.FuelPriceHistoryResponse;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationFuelPriceHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GetFuelPriceHistory {

    private final FuelStationFuelPriceHistoryRepository repository;

    public List<FuelPriceHistoryResponse> process(long fuelStationId) {
        return repository.findByFuelStationId(fuelStationId).stream()
                .map(entry -> new FuelPriceHistoryResponse(
                        entry.getFuelGrade(),
                        entry.getPricePerLiter(),
                        entry.getCurrency(),
                        entry.getChangedAt().toInstant(),
                        entry.getChangedBy()
                ))
                .toList();
    }

}
