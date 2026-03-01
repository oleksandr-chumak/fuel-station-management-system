package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa;

import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFuelStationEventRepository extends JpaRepository<FuelStationEventEntity, Long> {
    Page<FuelStationEventEntity> findByFuelStationId(Long fuelStationId, Pageable pageable);
    Page<FuelStationEventEntity> findByFuelStationIdAndEventType(Long fuelStationId, String eventType, Pageable pageable);
}
