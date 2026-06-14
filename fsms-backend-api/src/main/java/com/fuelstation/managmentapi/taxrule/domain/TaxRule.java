package com.fuelstation.managmentapi.taxrule.domain;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TaxRule(
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
) {}
