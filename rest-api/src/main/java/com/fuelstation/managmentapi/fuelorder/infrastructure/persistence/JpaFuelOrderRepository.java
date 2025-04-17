package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

@Repository
public interface JpaFuelOrderRepository extends JpaRepository<FuelOrderEntity, Long> {
    List<FuelOrderEntity> findByFuelStationId(Long fuelStationId); 
    
    @Query("SELECT SUM(f.amount) FROM FuelOrderEntity f WHERE f.fuelStationId = :stationId AND f.grade = :grade AND f.status NOT IN ('Confirmed', 'Rejected')")
    Float getUnconfirmedAmountByGradeAndStation(@Param("stationId") Long stationId, @Param("grade") FuelGrade grade);
}