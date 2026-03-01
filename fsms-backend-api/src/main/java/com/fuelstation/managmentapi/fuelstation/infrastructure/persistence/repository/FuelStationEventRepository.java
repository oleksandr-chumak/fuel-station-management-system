package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationEvent;

import java.time.Instant;
import java.util.List;

public interface FuelStationEventRepository {
    FuelStationEvent save(FuelStationEvent event);
    List<FuelStationEvent> findByFuelStationIdAfter(Long fuelStationId, Instant occurredAfter, int limit);
    List<FuelStationEvent> findLatestByFuelStationId(Long fuelStationId, int limit);
}
