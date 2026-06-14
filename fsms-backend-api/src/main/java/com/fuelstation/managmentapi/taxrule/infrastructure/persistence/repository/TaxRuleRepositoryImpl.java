package com.fuelstation.managmentapi.taxrule.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.taxrule.domain.TaxRule;
import com.fuelstation.managmentapi.taxrule.infrastructure.persistence.mapper.TaxRuleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class TaxRuleRepositoryImpl implements TaxRuleRepository {

    private final JpaTaxRuleRepository jpaTaxRuleRepository;
    private final TaxRuleMapper taxRuleMapper;

    @Override
    public List<TaxRule> findByCountryCode(CountryCode countryCode) {
        return jpaTaxRuleRepository.findByCountryId(countryCode.getId()).stream()
                .map(taxRuleMapper::toDomain)
                .toList();
    }

    @Override
    public List<TaxRule> findEffectiveByCountryCode(CountryCode countryCode) {
        return jpaTaxRuleRepository.findEffectiveByCountryId(countryCode.getId()).stream()
                .map(taxRuleMapper::toDomain)
                .toList();
    }
}
