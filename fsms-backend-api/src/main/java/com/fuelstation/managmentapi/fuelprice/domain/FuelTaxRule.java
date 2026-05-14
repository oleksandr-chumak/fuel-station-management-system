package com.fuelstation.managmentapi.fuelprice.domain;

import com.fuelstation.managmentapi.common.domain.CountryCode;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record FuelTaxRule(
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
