package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.jpa;

import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.model.FuelPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaFuelPriceRepository extends JpaRepository<FuelPriceEntity, Long> {

    @Query("""
        SELECT DISTINCT f
        FROM FuelPriceEntity f
        WHERE f.fetchedAt = (
                SELECT MAX(f2.fetchedAt)
                FROM FuelPriceEntity f2
                WHERE f2.fuelGradeId = f.fuelGradeId
        )
        """)
    List<FuelPriceEntity> findLatest();

}
