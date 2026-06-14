package com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.mapper;

import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.model.FuelGradeEntity;
import org.springframework.stereotype.Component;

@Component
public class FuelGradeMapper {

    public FuelGrade toDomain(FuelGradeEntity entity) {
        return FuelGrade.fromId(entity.getFuelGradeId());
    }
}
