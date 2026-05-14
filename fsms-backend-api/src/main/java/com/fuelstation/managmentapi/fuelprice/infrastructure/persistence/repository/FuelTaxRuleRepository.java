package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.common.domain.CountryCode;
import com.fuelstation.managmentapi.fuelprice.domain.FuelTaxRule;

import java.util.List;

public interface FuelTaxRuleRepository {

    List<FuelTaxRule> findEffectiveByCountyCode(CountryCode countryCode);
}
