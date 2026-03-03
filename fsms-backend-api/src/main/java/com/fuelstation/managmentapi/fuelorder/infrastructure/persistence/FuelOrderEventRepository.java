package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderEvent;
import org.springframework.data.domain.Page;

import java.time.Instant;

public interface FuelOrderEventRepository {
    FuelOrderEvent save(FuelOrderEvent event);
    Page<FuelOrderEvent> findByFuelStationIdAfter(Long fuelStationId, Instant occurredAfter, int limit);
}
