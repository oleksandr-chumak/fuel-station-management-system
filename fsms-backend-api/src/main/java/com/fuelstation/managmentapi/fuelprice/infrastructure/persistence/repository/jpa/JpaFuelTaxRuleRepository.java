package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.jpa;

import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.model.FuelTaxRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaFuelTaxRuleRepository extends JpaRepository<FuelTaxRuleEntity, Long> {

    @Query("""
        SELECT r FROM FuelTaxRuleEntity r
        WHERE r.countryId = :countryId
        AND r.effectiveFrom <= CURRENT_TIMESTAMP
        AND (r.effectiveTo IS NULL OR r.effectiveTo >= CURRENT_TIMESTAMP)
        """)
    List<FuelTaxRuleEntity> findEffectiveByCountryId(@Param("countryId") Long countryId);
}
