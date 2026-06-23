package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.models.*;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelPriceEmbeddable;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationAddressEmbeddable;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationEntity;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelTankEntity;
import org.springframework.stereotype.Component;

@Component
public class FuelStationMapper {

    public FuelStation toDomain(FuelStationEntity entity) {
        FuelStationAddressEmbeddable addressEmbeddable = entity.getAddress();
        FuelStationAddress address = new FuelStationAddress(
                addressEmbeddable.getStreet(),
                addressEmbeddable.getBuildingNumber(),
                addressEmbeddable.getCity(),
                addressEmbeddable.getPostalCode(),
                CountryCode.fromId(addressEmbeddable.getCountryId())
        );

        List<FuelTank> fuelTanks = entity.getFuelTanks().stream()
                .filter(tankEntity -> FuelTankStatus.fromId(tankEntity.getFuelTankStatusId()) == FuelTankStatus.ACTIVE)
                .map(tankEntity -> new FuelTank(
                        tankEntity.getFuelTankId(),
                        FuelGrade.fromId(tankEntity.getFuelGradeId()),
                        tankEntity.getCurrentVolume(),
                        tankEntity.getMaxCapacity(),
                        tankEntity.getLastRefillDate() != null ?
                                Optional.of(tankEntity.getLastRefillDate()) :
                                Optional.empty(),
                        FuelTankStatus.fromId(tankEntity.getFuelTankStatusId())
                ))
                .collect(Collectors.toList());

        List<FuelStationFuelPrice> fuelPrices = entity.getFuelPrices().stream()
                .map(priceEmbeddable -> new FuelStationFuelPrice(
                        FuelGrade.fromId(priceEmbeddable.getFuelGradeId()),
                        priceEmbeddable.getPricePerLiter(),
                        CurrencyCode.fromString(priceEmbeddable.getCurrency())
                ))
                .collect(Collectors.toList());

        List<Long> managerIds = entity.getAssignedManagers();

        return new FuelStation(
                entity.getFuelStationId(),
                address,
                fuelTanks,
                fuelPrices,
                managerIds,
                FuelStationStatus.fromId(entity.getFuelStationStatusId()),
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
                address.country().getId()
        );

        FuelStationEntity entity = new FuelStationEntity();
        entity.setFuelStationId(domain.getFuelStationId());
        entity.setAddress(addressEmbeddable);
        entity.setFuelStationStatusId(domain.getStatus().getId());
        entity.setCreatedAt(domain.getCreatedAt());

        List<FuelPriceEmbeddable> fuelPriceEmbeddables = domain.getFuelPrices().stream()
                .map(price -> new FuelPriceEmbeddable(
                        price.fuelGrade().getId(),
                        price.pricePerLiter(),
                        price.currency().name()
                ))
                .collect(Collectors.toList());
        entity.setFuelPrices(fuelPriceEmbeddables);

        entity.setAssignedManagers(domain.getAssignedManagersIds());

        List<FuelTankEntity> fuelTankEntities = domain.getFuelTanks().stream()
                .map(tank -> new FuelTankEntity(
                        tank.getId(),
                        tank.getFuelGrade().getId(),
                        tank.getCurrentVolume(),
                        tank.getMaxCapacity(),
                        entity,
                        tank.getLastRefillDate().orElse(null),
                        tank.getStatus().getId()
                ))
                .collect(Collectors.toList());

        entity.setFuelTanks(fuelTankEntities);

        return entity;
    }
}