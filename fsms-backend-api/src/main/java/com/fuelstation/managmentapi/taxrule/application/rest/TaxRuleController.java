package com.fuelstation.managmentapi.taxrule.application.rest;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.taxrule.application.rest.response.TaxRuleResponse;
import com.fuelstation.managmentapi.taxrule.application.query.ListTaxRulesByCountryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaxRuleController {

    private final ListTaxRulesByCountryQuery listTaxRulesByCountryQuery;

    @GetMapping("/countries/{countryCode}/tax-rules")
    public ResponseEntity<List<TaxRuleResponse>> getTaxRulesByCountry(
            @PathVariable CountryCode countryCode,
            @RequestParam(value = "effective", defaultValue = "false") boolean effective
    ) {
        return ResponseEntity.ok(listTaxRulesByCountryQuery.handle(countryCode, effective));
    }
}
