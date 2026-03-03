package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationEvent;
import org.springframework.data.domain.Page;

import java.time.Instant;

public interface FuelStationEventRepository {
    FuelStationEvent save(FuelStationEvent event);
    Page<FuelStationEvent> findByFuelStationIdAfter(Long fuelStationId, Instant occurredAfter, int limit);
}
