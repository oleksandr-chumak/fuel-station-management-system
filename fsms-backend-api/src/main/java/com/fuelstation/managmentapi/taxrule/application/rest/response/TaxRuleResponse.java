package com.fuelstation.managmentapi.taxrule.application.rest.response;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.taxrule.domain.TaxRule;
import com.fuelstation.managmentapi.taxrule.domain.TaxType;
import com.fuelstation.managmentapi.taxrule.domain.TaxUnit;
import com.fuelstation.managmentapi.taxrule.domain.TaxValueType;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TaxRuleResponse(
    Long taxRuleId,
    CountryCode countryCode,
    FuelGrade fuelGrade,
    TaxType taxType,
    String nameLocal,
    String nameEnglish,
    String description,
    TaxValueType valueType,
    BigDecimal amount,
    @Nullable CurrencyCode currency,
    @Nullable TaxUnit unit,
    OffsetDateTime effectiveFrom,
    @Nullable OffsetDateTime effectiveTo
) {
    public static TaxRuleResponse fromDomain(TaxRule domain) {
        return new TaxRuleResponse(
            domain.taxRuleId(),
            domain.countryCode(),
            domain.fuelGrade(),
            domain.taxType(),
            domain.nameLocal(),
            domain.nameEnglish(),
            domain.description(),
            domain.valueType(),
            domain.amount(),
            domain.currency(),
            domain.unit(),
            domain.effectiveFrom(),
            domain.effectiveTo()
        );
    }
}
