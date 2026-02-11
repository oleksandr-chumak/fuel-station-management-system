package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;

@Component
public class FuelOrderMapper {

    public FuelOrder toDomain(FuelOrderEntity entity) {
        return new FuelOrder(
                entity.getFuelOrderId(),
                FuelOrderStatus.fromId(entity.getFuelOrderStatusId()),
                FuelGrade.fromId(entity.getFuelGradeId()),
                entity.getAmount(),
                entity.getFuelStationId(),
                entity.getCreatedAt()
        );
    }

    public FuelOrderEntity toEntity(FuelOrder domain) {
        return new FuelOrderEntity(
                domain.getFuelOrderId(),
                domain.getStatus().getId(),
                domain.getGrade().getId(),
                domain.getAmount(),
                domain.getFuelStationId(),
                domain.getCreatedAt()
        );
    }
}
