package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface JpaFuelOrderEventRepository extends JpaRepository<FuelOrderEventEntity, Long> {

    @Query("SELECT e FROM FuelOrderEventEntity e WHERE e.fuelStationId = :fuelStationId AND e.occurredAt < :occurredAfter")
    Page<FuelOrderEventEntity> findByFuelStationIdAndOccurredAt(@Param("fuelStationId") Long fuelStationId, @Param("occurredAfter") OffsetDateTime occurredAfter, Pageable pageable);
}