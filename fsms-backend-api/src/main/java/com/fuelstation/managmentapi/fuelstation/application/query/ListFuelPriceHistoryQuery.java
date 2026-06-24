package com.fuelstation.managmentapi.fuelstation.application.query;

import com.fuelstation.managmentapi.fuelstation.application.rest.response.FuelPriceHistoryResponse;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationFuelPriceHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
public class ListFuelPriceHistoryQuery {

    private final FuelStationFuelPriceHistoryRepository repository;

    public List<FuelPriceHistoryResponse> process(long fuelStationId, Instant from) {
        return repository.findRealChanges(fuelStationId, from).stream()
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
