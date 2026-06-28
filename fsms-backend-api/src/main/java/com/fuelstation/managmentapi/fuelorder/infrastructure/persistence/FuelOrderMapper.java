package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderAllocation;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class FuelOrderMapper {

    public FuelOrder toDomain(FuelOrderEntity entity) {
        return new FuelOrder(
                entity.getFuelOrderId(),
                entity.getFuelStationId(),
                entity.getAllocations().stream()
                        .map(a -> new FuelOrderAllocation(a.getFuelTankId(), a.getVolume()))
                        .toList(),
                FuelOrderStatus.fromId(entity.getFuelOrderStatusId()),
                FuelGrade.fromId(entity.getFuelGradeId()),
                entity.getCreatedAt(),
                entity.getCreatedBy()
        );
    }

    public FuelOrderEntity toEntity(FuelOrder domain) {
        return new FuelOrderEntity(
                domain.getFuelOrderId(),
                domain.getFuelStationId(),
                domain.getAllocations().stream()
                        .map(a -> new FuelOrderAllocationEmbeddable(a.fuelTankId(), a.volume()))
                        .collect(java.util.stream.Collectors.toCollection(ArrayList::new)),
                domain.getStatus().getId(),
                domain.getGrade().getId(),
                domain.getCreatedAt(),
                domain.getCreatedBy()
        );
    }
}
