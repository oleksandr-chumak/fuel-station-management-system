package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa;

import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface JpaFuelStationEventRepository extends JpaRepository<FuelStationEventEntity, Long> {

    @Query("SELECT e FROM FuelStationEventEntity e WHERE e.fuelStationId = :fuelStationId AND e.occurredAt < :occurredAfter")
    Page<FuelStationEventEntity> findByFuelStationIdAndOccurredAt(
            @Param("fuelStationId") Long fuelStationId,
            @Param("occurredAfter") OffsetDateTime occurredAfter,
            Pageable pageable
    );
}