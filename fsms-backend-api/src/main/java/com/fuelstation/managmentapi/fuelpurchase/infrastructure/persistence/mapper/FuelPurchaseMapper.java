package com.fuelstation.managmentapi.fuelpurchase.infrastructure.persistence.mapper;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelpurchase.domain.FuelPurchase;
import com.fuelstation.managmentapi.fuelpurchase.infrastructure.persistence.model.FuelPurchaseEntity;
import org.springframework.stereotype.Component;

@Component
public class FuelPurchaseMapper {

    public FuelPurchase toDomain(FuelPurchaseEntity entity) {
        return new FuelPurchase(
                entity.getFuelPurchaseId(),
                entity.getFuelOrderId(),
                entity.getFuelStationId(),
                FuelGrade.fromId(entity.getFuelGradeId()),
                entity.getAmount(),
                entity.getPricePerLiter(),
                CurrencyCode.fromString(entity.getCurrency()),
                entity.getTotalPrice(),
                entity.getPurchasedAt()
        );
    }

    public FuelPurchaseEntity toEntity(FuelPurchase domain) {
        return FuelPurchaseEntity.builder()
                .fuelPurchaseId(domain.getFuelPurchaseId())
                .fuelOrderId(domain.getFuelOrderId())
                .fuelStationId(domain.getFuelStationId())
                .fuelGradeId(domain.getFuelGrade().getId())
                .amount(domain.getAmount())
                .pricePerLiter(domain.getPricePerLiter())
                .currency(domain.getCurrency().name())
                .totalPrice(domain.getTotalPrice())
                .purchasedAt(domain.getPurchasedAt())
                .build();
    }
}
