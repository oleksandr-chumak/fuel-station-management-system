package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaManagerRepository extends JpaRepository<ManagerEntity, Long> {
    /**
     * Find all managers assigned to a specific fuel station
     * @param fuelStationId The ID of the fuel station
     * @return List of managers assigned to the fuel station
     */
    @Query("SELECT m FROM ManagerEntity m JOIN m.fuelStationsAssignedTo fs WHERE fs.id = :fuelStationId")
    List<ManagerEntity> findManagersByFuelStationId(@Param("fuelStationId") Long fuelStationId);
}
