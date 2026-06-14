package com.fuelstation.managmentapi.taxrule.application.query;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.taxrule.application.rest.response.TaxRuleResponse;
import com.fuelstation.managmentapi.taxrule.infrastructure.persistence.repository.TaxRuleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListTaxRulesByCountryQuery {

    private final TaxRuleRepository taxRuleRepository;

    public List<TaxRuleResponse> handle(CountryCode countryCode, boolean effectiveOnly) {
        var rules = effectiveOnly
                ? taxRuleRepository.findEffectiveByCountryCode(countryCode)
                : taxRuleRepository.findByCountryCode(countryCode);
        return rules.stream()
                .map(TaxRuleResponse::fromDomain)
                .toList();
    }
}
