package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaFuelStationRepository extends JpaRepository<FuelStationEntity, Long> {

    @Query("SELECT f FROM FuelStationEntity f JOIN f.assignedManagers m WHERE m = :managerId")
    List<FuelStationEntity> findByManagerId(@Param("managerId") Long managerId);

    @Query("SELECT f FROM FuelStationEntity f ORDER BY f.fuelStationStatusId ASC, f.createdAt DESC")
    List<FuelStationEntity> findAll();
}
