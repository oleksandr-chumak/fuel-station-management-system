package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;

public class FuelOrderMapper {
    public static FuelOrder toDomain(FuelOrderEntity entity) {
        if (entity == null) return null;
        return new FuelOrder(
            entity.getId(),
            entity.getStatus(),
            entity.getGrade(),
            entity.getAmount(),
            entity.getFuelStationId(), // Changed field name to match the entity
            entity.getCreatedAt()
        );
    }

    public static FuelOrderEntity toEntity(FuelOrder domain) {
        if (domain == null) return null;
        return new FuelOrderEntity(
            domain.getId(),
            domain.getStatus(),
            domain.getGrade(),
            domain.getAmount(),
            domain.getGasStationId(), // Domain model still uses gasStationId - this line maps it to fuelStationId in the entity
            domain.getCreatedAt()
        );
    }
}
