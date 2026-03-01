package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderEvent;

import java.time.Instant;
import java.util.List;

public interface FuelOrderEventRepository {
    FuelOrderEvent save(FuelOrderEvent event);
    List<FuelOrderEvent> findByFuelStationIdAfter(Long fuelStationId, Instant occurredAfter, int limit);
    List<FuelOrderEvent> findLatestByFuelStationId(Long fuelStationId, int limit);
}
