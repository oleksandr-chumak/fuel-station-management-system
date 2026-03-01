package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationEvent;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FuelStationEventRepository {
    Page<FuelStationEvent> findByFuelStationId(Long fuelStationId, Pageable pageable);
    Page<FuelStationEvent> findByFuelStationIdAndEventType(
            Long fuelStationId,
            FuelStationEventType eventType,
            Pageable pageable
    );
}
