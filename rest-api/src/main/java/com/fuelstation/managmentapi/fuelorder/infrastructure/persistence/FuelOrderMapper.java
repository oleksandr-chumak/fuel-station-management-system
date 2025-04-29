package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationEntity;

import jakarta.persistence.EntityManager;

@Component
public class FuelOrderMapper {

    @Autowired
    private EntityManager em;

    public FuelOrder toDomain(FuelOrderEntity entity) {
        if (entity == null) return null;
        return new FuelOrder(
            entity.getId(),
            entity.getStatus(),
            entity.getGrade(),
            entity.getAmount(),
            entity.getFuelStationId(), 
            entity.getCreatedAt()
        );
    }

    public FuelOrderEntity toEntity(FuelOrder domain) {
        if (domain == null) return null;
        return new FuelOrderEntity(
            domain.getId(),
            domain.getStatus(),
            domain.getGrade(),
            domain.getAmount(),
            em.getReference(FuelStationEntity.class, domain.getFuelStationId()),
            domain.getFuelStationId(), 
            domain.getCreatedAt()
        );
    }
}
