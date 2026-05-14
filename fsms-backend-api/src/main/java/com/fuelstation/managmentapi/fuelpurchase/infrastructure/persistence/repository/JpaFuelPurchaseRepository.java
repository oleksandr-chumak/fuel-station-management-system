package com.fuelstation.managmentapi.fuelpurchase.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelpurchase.infrastructure.persistence.model.FuelPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaFuelPurchaseRepository extends JpaRepository<FuelPurchaseEntity, Long> {

    @Query("SELECT p FROM FuelPurchaseEntity p WHERE p.fuelStationId = :stationId ORDER BY p.purchasedAt DESC")
    List<FuelPurchaseEntity> findByFuelStationId(@Param("stationId") long stationId);
}
