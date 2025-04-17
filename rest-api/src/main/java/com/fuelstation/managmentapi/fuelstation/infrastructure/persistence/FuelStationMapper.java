package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelPrice;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationAddress;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTank;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerEntity;

import jakarta.persistence.EntityManager;

@Component
public class FuelStationMapper {

    @Autowired
    private EntityManager em;
    
    /**
     * Converts a FuelStationEntity to a FuelStation domain object
     * 
     * @param entity The entity to convert
     * @return The domain object
     */
    public FuelStation toDomain(FuelStationEntity entity) {
        if (entity == null) {
            return null;
        }

        // Map address
        FuelStationAddressEmbeddable addressEmbeddable = entity.getAddress();
        FuelStationAddress address = new FuelStationAddress(
            addressEmbeddable.getStreet(),
            addressEmbeddable.getBuildingNumber(),
            addressEmbeddable.getCity(),
            addressEmbeddable.getPostalCode(),
            addressEmbeddable.getCountry()
        );

        // Map fuel tanks
        List<FuelTank> fuelTanks = entity.getFuelTanks().stream()
            .map(tankEntity -> new FuelTank(
                tankEntity.getId(),
                tankEntity.getFuelGrade(),
                tankEntity.getCurrentVolume(),
                tankEntity.getMaxCapacity(),
                tankEntity.getLastRefillDate() != null ? 
                    Optional.of(tankEntity.getLastRefillDate()) : 
                    Optional.empty()
            ))
            .collect(Collectors.toList());

        // Map fuel prices
        List<FuelPrice> fuelPrices = entity.getFuelPrices().stream()
            .map(priceEmbeddable -> new FuelPrice(
                priceEmbeddable.getFuelGrade(),
                priceEmbeddable.getPricePerLiter()
            ))
            .collect(Collectors.toList());

        // Map manager IDs
        List<Long> managerIds = entity.getAssignedManagers().stream()
            .map(ManagerEntity::getId)
            .collect(Collectors.toList());

        return new FuelStation(
            entity.getId(),
            address,
            fuelTanks,
            fuelPrices,
            managerIds,
            entity.getStatus(),
            entity.getCreatedAt()
        );
    }

    /**
     * Converts a FuelStation domain object to a FuelStationEntity
     * 
     * @param domain The domain object to convert
     * @return The entity
     */
    public FuelStationEntity toEntity(FuelStation domain) {
        if (domain == null) {
            return null;
        }

        // Map address
        FuelStationAddress address = domain.getAddress();
        FuelStationAddressEmbeddable addressEmbeddable = new FuelStationAddressEmbeddable(
            address.street(),
            address.buildingNumber(),
            address.city(),
            address.postalCode(),
            address.country()
        );

        // Create entity with basic properties
        FuelStationEntity entity = new FuelStationEntity();
        entity.setId(domain.getId());
        entity.setAddress(addressEmbeddable);
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());

        // Map fuel prices
        List<FuelPriceEmbeddable> fuelPriceEmbeddables = domain.getFuelPrices().stream()
            .map(price -> new FuelPriceEmbeddable(
                price.fuelGrade(),
                price.pricePerLiter(),
                domain.getId()
            ))
            .collect(Collectors.toList());
        entity.setFuelPrices(fuelPriceEmbeddables);

        // Filter and add assigned managers based on ID
        List<ManagerEntity> assignedManagerEntities = domain.getAssignedManagersIds().stream()
            .map(id -> em.getReference(ManagerEntity.class, id))
            .collect(Collectors.toList());
        entity.setAssignedManagers(assignedManagerEntities);

        // Map fuel tanks - needs to be done after entity is created to establish the relationship
        List<FuelTankEntity> fuelTankEntities = domain.getFuelTanks().stream()
            .map(tank -> {
                FuelTankEntity tankEntity = new FuelTankEntity(
                    tank.getId(),
                    tank.getFuelGrade(),
                    tank.getCurrentVolume(),
                    tank.getMaxCapacity(),
                    entity,
                    tank.getLastRefillDate().orElse(null)
                );
                return tankEntity;
            })
            .collect(Collectors.toList());
        entity.setFuelTanks(fuelTankEntities);

        return entity;
    }
}