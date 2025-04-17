package com.fuelstation.managmentapi.fuelstation.domain;

import java.time.LocalDate;
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
public class DomainFuelStationFactory implements FuelStationFactory{

    @Override
    public FuelStation create(String street, String buildingNumber, String city, String postalCode, String country) {
        FuelStationAddress address = new FuelStationAddress(street, buildingNumber, city, postalCode, country);

        List<FuelTank> fuelTanks = new ArrayList<>();
        fuelTanks.add(new FuelTank(null, FuelGrade.Diesel, 0f, 35000f, Optional.empty()));
        fuelTanks.add(new FuelTank(null, FuelGrade.RON_92, 0f, 35000f, Optional.empty()));
        fuelTanks.add(new FuelTank(null, FuelGrade.RON_95, 0f, 35000f, Optional.empty()));

        List<FuelPrice> fuelPrices = new ArrayList<>();
        fuelPrices.add(new FuelPrice(FuelGrade.Diesel, 1.21f));
        fuelPrices.add(new FuelPrice(FuelGrade.RON_92, 1.78f));
        fuelPrices.add(new FuelPrice(FuelGrade.RON_95, 2.31f));

        return new FuelStation(null, address, fuelTanks, fuelPrices, new ArrayList<>(), FuelStationStatus.Active, LocalDate.now());
    }
    
}
