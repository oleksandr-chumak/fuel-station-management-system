package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFuelOrderRepository extends JpaRepository<FuelOrderEntity, Long> {
    List<FuelOrderEntity> findByFuelStationId(Long fuelStationId); 
    
    @Query("SELECT SUM(f.amount) FROM FuelOrderEntity f WHERE f.fuelStationId = :stationId AND f.fuelGradeId = :fuelGradeId AND f.fuelOrderId NOT IN (2, 3)")
    BigDecimal getUnconfirmedAmountByGradeAndStation(@Param("stationId") Long stationId, @Param("fuelGradeId") Long fuelGradeId);
}