package com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.model.FuelGradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaFuelGradeRepository extends JpaRepository<FuelGradeEntity, Long> {

    @Query("""
        SELECT g FROM FuelGradeEntity g
        WHERE g.fuelGradeId IN (
            SELECT a.fuelGradeId FROM AvailableFuelGradeEntity a
            WHERE a.countryId = :countryId
        )
        ORDER BY g.fuelGradeId
        """)
    List<FuelGradeEntity> findAvailableByCountryId(@Param("countryId") Long countryId);
}
