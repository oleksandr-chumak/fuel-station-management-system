package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelPrice;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationAddress;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTank;

import io.jsonwebtoken.lang.Arrays;

@Component
public class FuelStationMapper {

    public FuelStation toDomain(FuelStationEntity entity) {
        FuelStationAddressEmbeddable addressEmbeddable = entity.getAddress();
        FuelStationAddress address = new FuelStationAddress(
            addressEmbeddable.getStreet(),
            addressEmbeddable.getBuildingNumber(),
            addressEmbeddable.getCity(),
            addressEmbeddable.getPostalCode(),
            addressEmbeddable.getCountry()
        );

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

        List<FuelPrice> fuelPrices = entity.getFuelPrices().stream()
            .map(priceEmbeddable -> new FuelPrice(
                priceEmbeddable.getFuelGrade(),
                priceEmbeddable.getPricePerLiter()
            ))
            .collect(Collectors.toList());

        List<Long> managerIds = Arrays.asList(entity.getAssignedManagers());

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

    public FuelStationEntity toEntity(FuelStation domain) {

        FuelStationAddress address = domain.getAddress();
        FuelStationAddressEmbeddable addressEmbeddable = new FuelStationAddressEmbeddable(
            address.street(),
            address.buildingNumber(),
            address.city(),
            address.postalCode(),
            address.country()
        );

        FuelStationEntity entity = new FuelStationEntity();
        entity.setId(domain.getId());
        entity.setAddress(addressEmbeddable);
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());

        List<FuelPriceEmbeddable> fuelPriceEmbeddables = domain.getFuelPrices().stream()
            .map(price -> new FuelPriceEmbeddable(
                price.fuelGrade(),
                price.pricePerLiter(),
                domain.getId()
            ))
            .collect(Collectors.toList());
        entity.setFuelPrices(fuelPriceEmbeddables);

        entity.setAssignedManagers(domain.getAssignedManagersIds().toArray(new Long[domain.getAssignedManagersIds().size()]));

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