package com.fuelstation.managmentapi.taxrule.infrastructure.persistence.mapper;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.taxrule.domain.TaxRule;
import com.fuelstation.managmentapi.taxrule.domain.TaxType;
import com.fuelstation.managmentapi.taxrule.domain.TaxUnit;
import com.fuelstation.managmentapi.taxrule.domain.TaxValueType;
import com.fuelstation.managmentapi.taxrule.infrastructure.persistence.model.TaxRuleEntity;
import org.springframework.stereotype.Component;

@Component
public class TaxRuleMapper {

    public TaxRule toDomain(TaxRuleEntity entity) {
        return new TaxRule(
                entity.getTaxRuleId(),
                CountryCode.fromId(entity.getCountryId()),
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
