package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.mapper;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelprice.domain.FuelPrice;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.model.FuelPriceEntity;
import org.springframework.stereotype.Component;

@Component
public class FuelPriceMapper {

    public FuelPrice toDomain(FuelPriceEntity entity) {
        return new FuelPrice(
            entity.getFuelPriceId(),
            FuelGrade.fromId(entity.getFuelGradeId()),
            entity.getUnit(),
            entity.getPrice(),
            entity.getCurrencyCode(),
            entity.getSource(),
            entity.getFetchedAt()
        );
    }

    public FuelPriceEntity fromDomain(FuelPrice domain) {
        return new FuelPriceEntity(
            domain.fuelPriceId(),
            domain.fuelGrade().getId(),
            domain.unit(),
            domain.price(),
            domain.currency(),
            domain.source(),
            domain.fetchedAt()
        );
    }
}
