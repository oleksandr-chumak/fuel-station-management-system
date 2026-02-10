package com.fuelstation.managmentapi.fuelstation.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelPrice;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationAddress;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTank;

@Component
public class DomainFuelStationFactory implements FuelStationFactory {

    @Override
    public FuelStation create(String street, String buildingNumber, String city, String postalCode, String country) {
        FuelStationAddress address = new FuelStationAddress(street, buildingNumber, city, postalCode, country);

        List<FuelTank> fuelTanks = new ArrayList<>();
        fuelTanks.add(new FuelTank(null, FuelGrade.DIESEL, BigDecimal.ZERO, BigDecimal.valueOf(35000), Optional.empty()));
        fuelTanks.add(new FuelTank(null, FuelGrade.RON_92, BigDecimal.ZERO, BigDecimal.valueOf(35000), Optional.empty()));
        fuelTanks.add(new FuelTank(null, FuelGrade.RON_95, BigDecimal.ZERO, BigDecimal.valueOf(35000), Optional.empty()));

        List<FuelPrice> fuelPrices = new ArrayList<>();
        fuelPrices.add(new FuelPrice(FuelGrade.DIESEL, BigDecimal.valueOf(1.21f)));
        fuelPrices.add(new FuelPrice(FuelGrade.RON_92, BigDecimal.valueOf(1.78f)));
        fuelPrices.add(new FuelPrice(FuelGrade.RON_95, BigDecimal.valueOf(2.31f)));

        return new FuelStation(null, address, fuelTanks, fuelPrices, new ArrayList<>(), FuelStationStatus.ACTIVE, OffsetDateTime.now());
    }

}
