package com.fuelstation.managmentapi.taxrule.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.taxrule.infrastructure.persistence.model.TaxRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaTaxRuleRepository extends JpaRepository<TaxRuleEntity, Long> {

    @Query("""
        SELECT r FROM TaxRuleEntity r
        WHERE r.countryId = :countryId
        ORDER BY r.effectiveFrom DESC
        """)
    List<TaxRuleEntity> findByCountryId(@Param("countryId") Long countryId);

    @Query("""
        SELECT r FROM TaxRuleEntity r
        WHERE r.countryId = :countryId
        AND r.effectiveFrom <= CURRENT_TIMESTAMP
        AND (r.effectiveTo IS NULL OR r.effectiveTo >= CURRENT_TIMESTAMP)
        """)
    List<TaxRuleEntity> findEffectiveByCountryId(@Param("countryId") Long countryId);
}
