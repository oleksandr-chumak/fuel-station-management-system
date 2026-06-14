package com.fuelstation.managmentapi.taxrule.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.taxrule.domain.TaxRule;

import java.util.List;

public interface TaxRuleRepository {

    List<TaxRule> findByCountryCode(CountryCode countryCode);

    List<TaxRule> findEffectiveByCountryCode(CountryCode countryCode);
}
