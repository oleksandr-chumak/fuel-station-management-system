package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.impl;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.fuelprice.domain.FuelTaxRule;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.mapper.FuelTaxRuleMapper;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.FuelTaxRuleRepository;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.jpa.JpaFuelTaxRuleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class FuelTaxRuleRepositoryImpl implements FuelTaxRuleRepository {

    private final JpaFuelTaxRuleRepository jpaFuelTaxRuleRepository;
    private final FuelTaxRuleMapper fuelTaxRuleMapper;

    @Override
    public List<FuelTaxRule> findEffectiveByCountyCode(CountryCode countryCode) {
        return jpaFuelTaxRuleRepository
            .findEffectiveByCountryId(countryCode.getId())
            .stream()
            .map(fuelTaxRuleMapper::toDomain)
            .toList();
    }
}
