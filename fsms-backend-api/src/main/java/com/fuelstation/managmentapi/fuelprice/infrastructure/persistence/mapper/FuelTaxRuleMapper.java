package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.mapper;

import com.fuelstation.managmentapi.common.domain.CountryCode;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelprice.domain.FuelTaxRule;
import com.fuelstation.managmentapi.fuelprice.domain.TaxType;
import com.fuelstation.managmentapi.fuelprice.domain.TaxUnit;
import com.fuelstation.managmentapi.fuelprice.domain.TaxValueType;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.model.FuelTaxRuleEntity;
import org.springframework.stereotype.Component;

@Component
public class FuelTaxRuleMapper {

    public FuelTaxRule toDomain(FuelTaxRuleEntity entity) {
        return new FuelTaxRule(
                entity.getTaxRuleId(),
                CountryCode.valueOf(entity.getCountryCode()),
                FuelGrade.fromId(entity.getFuelGradeId()),
                TaxType.valueOf(entity.getTaxType()),
                entity.getNameLocal(),
                entity.getNameEnglish(),
                entity.getDescription(),
                TaxValueType.valueOf(entity.getValueType()),
                entity.getAmount(),
                entity.getCurrency() != null ? CurrencyCode.fromString(entity.getCurrency()) : null,
                entity.getUnit() != null ? TaxUnit.valueOf(entity.getUnit()) : null,
                entity.getEffectiveFrom(),
                entity.getEffectiveTo()
        );
    }
}
