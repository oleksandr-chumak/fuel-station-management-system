package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface JpaFuelOrderEventRepository extends JpaRepository<FuelOrderEventEntity, Long> {

    @Query("SELECT e FROM FuelOrderEventEntity e WHERE e.fuelStationId = :fuelStationId AND e.occurredAt < :occurredAfter")
    List<FuelOrderEventEntity> findByFuelStationIdAndOccurredAt(@Param("fuelStationId") Long fuelStationId, @Param("occurredAfter") OffsetDateTime occurredAfter, Pageable pageable);

    @Query("SELECT e FROM FuelOrderEventEntity e WHERE e.fuelStationId = :fuelStationId")
    List<FuelOrderEventEntity> findByFuelStationId(@Param("fuelStationId") Long fuelStationId, Pageable pageable);
}