package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFuelOrderRepository extends JpaRepository<FuelOrderEntity, Long> {
    List<FuelOrderEntity> findByFuelStationId(Long fuelStationId); 
}