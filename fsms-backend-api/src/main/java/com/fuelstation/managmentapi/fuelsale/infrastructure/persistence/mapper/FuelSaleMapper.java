package com.fuelstation.managmentapi.fuelsale.infrastructure.persistence.mapper;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelsale.domain.FuelSale;
import com.fuelstation.managmentapi.fuelsale.infrastructure.persistence.model.FuelSaleEntity;
import org.springframework.stereotype.Component;

@Component
public class FuelSaleMapper {

    public FuelSale toDomain(FuelSaleEntity entity) {
        return new FuelSale(
                entity.getFuelSaleId(),
                entity.getFuelStationId(),
                entity.getFuelTankId(),
                FuelGrade.fromId(entity.getFuelGradeId()),
                entity.getVolume(),
                entity.getPricePerLiter(),
                CurrencyCode.fromString(entity.getCurrency()),
                entity.getTotalRevenue(),
                entity.getSoldAt()
        );
    }

    public FuelSaleEntity toEntity(FuelSale domain) {
        return FuelSaleEntity.builder()
                .fuelSaleId(domain.getFuelSaleId())
                .fuelStationId(domain.getFuelStationId())
                .fuelTankId(domain.getFuelTankId())
                .fuelGradeId(domain.getFuelGrade().getId())
                .volume(domain.getVolume())
                .pricePerLiter(domain.getPricePerLiter())
                .currency(domain.getCurrency().name())
                .totalRevenue(domain.getTotalRevenue())
                .soldAt(domain.getSoldAt())
                .build();
    }
}
