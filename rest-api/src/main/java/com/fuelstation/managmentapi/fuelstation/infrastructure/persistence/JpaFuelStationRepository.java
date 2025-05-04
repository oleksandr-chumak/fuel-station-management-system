package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaFuelStationRepository extends JpaRepository<FuelStationEntity, Long> {

    @Query(value = "SELECT * FROM fuel_stations WHERE :managerId = ANY (assigned_managers)", nativeQuery = true)
    List<FuelStationEntity> findByManagerId(@Param("managerId") Long managerId);
    
}
