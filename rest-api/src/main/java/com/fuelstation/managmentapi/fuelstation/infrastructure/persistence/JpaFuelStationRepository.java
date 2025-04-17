package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFuelStationRepository extends JpaRepository<FuelStationEntity, Long> {
}
