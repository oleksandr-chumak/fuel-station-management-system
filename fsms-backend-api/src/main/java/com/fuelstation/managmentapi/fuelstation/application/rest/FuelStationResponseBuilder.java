package com.fuelstation.managmentapi.fuelstation.application.rest;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderAllocation;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class FuelStationResponseBuilder {

    private final FuelOrderRepository fuelOrderRepository;

    public FuelStationResponse build(FuelStation fuelStation) {
        var pendingByTankId = pendingVolumesForStations(List.of(fuelStation.getFuelStationId()));
        return FuelStationResponse.fromDomain(fuelStation, pendingByTankId);
    }

    public List<FuelStationResponse> buildAll(List<FuelStation> fuelStations) {
        var stationIds = fuelStations.stream().map(FuelStation::getFuelStationId).toList();
        var pendingByTankId = pendingVolumesForStations(stationIds);
        return fuelStations.stream()
            .map(station -> FuelStationResponse.fromDomain(station, pendingByTankId))
            .toList();
    }

    private Map<Long, BigDecimal> pendingVolumesForStations(List<Long> stationIds) {
        Map<Long, BigDecimal> pendingByTankId = new HashMap<>();
        List<FuelOrder> pendingOrders = fuelOrderRepository.findPendingByFuelStationIds(stationIds);
        for (FuelOrder order : pendingOrders) {
            for (FuelOrderAllocation allocation : order.getAllocations()) {
                pendingByTankId.merge(allocation.fuelTankId(), allocation.volume(), BigDecimal::add);
            }
        }
        return pendingByTankId;
    }

}
