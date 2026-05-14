package com.fuelstation.managmentapi.fuelstation.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.CountryCode;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationFuelPrice;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationAddress;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTank;

@Component
public class DomainFuelStationFactory implements FuelStationFactory {

    @Override
    public FuelStation create(String street, String buildingNumber, String city, String postalCode, CountryCode country, List<FuelStationFuelPrice> initialPrices) {
        FuelStationAddress address = new FuelStationAddress(street, buildingNumber, city, postalCode, country);

        List<FuelTank> fuelTanks = new ArrayList<>();
        fuelTanks.add(new FuelTank(null, FuelGrade.DIESEL, BigDecimal.ZERO, BigDecimal.valueOf(35000), Optional.empty()));
        fuelTanks.add(new FuelTank(null, FuelGrade.RON_92, BigDecimal.ZERO, BigDecimal.valueOf(35000), Optional.empty()));
        fuelTanks.add(new FuelTank(null, FuelGrade.RON_95, BigDecimal.ZERO, BigDecimal.valueOf(35000), Optional.empty()));

        return new FuelStation(null, address, fuelTanks, new ArrayList<>(initialPrices), new ArrayList<>(), FuelStationStatus.ACTIVE, OffsetDateTime.now());
    }

}
