package com.fuelstation.managmentapi.fuelprice.application.query.model;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelprice.domain.FuelTaxRule;
import com.fuelstation.managmentapi.fuelprice.domain.TaxType;
import com.fuelstation.managmentapi.fuelprice.domain.TaxUnit;
import com.fuelstation.managmentapi.fuelprice.domain.TaxValueType;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record FuelTaxRuleResponse(
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
    public static FuelTaxRuleResponse from(FuelTaxRule domain) {
        return new FuelTaxRuleResponse(
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
