package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;

@Component
public class FuelOrderMapper {

    public FuelOrder toDomain(FuelOrderEntity entity) {
        return new FuelOrder(
            entity.getFuelOrderId(),
            entity.getStatus(),
            entity.getGrade(),
            entity.getAmount(),
            entity.getFuelStationId(), 
            entity.getCreatedAt()
        );
    }

    public FuelOrderEntity toEntity(FuelOrder domain) {
        return new FuelOrderEntity(
            domain.getFuelOrderId(),
            domain.getStatus(),
            domain.getGrade(),
            domain.getAmount(),
            domain.getFuelStationId(), 
            domain.getCreatedAt()
        );
    }
}
