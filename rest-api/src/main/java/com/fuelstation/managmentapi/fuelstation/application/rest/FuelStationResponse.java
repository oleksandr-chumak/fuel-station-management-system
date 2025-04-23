package com.fuelstation.managmentapi.fuelstation.application.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelPrice;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelStationResponse {
    private Long id;
    private String street;
    private String buildingNumber;
    private String city;
    private String postalCode;
    private List<Long> assignedManagersIds;
    private List<FuelTank> fuelTanks;
    private List<FuelPrice> fuelPrices;
    private String country;
    private FuelStationStatus status;
    
    public static FuelStationResponse fromDomain(FuelStation fuelStation) {
        FuelStationResponse response = new FuelStationResponse();
        response.setId(fuelStation.getId());
        response.setStreet(fuelStation.getAddress().street());
        response.setFuelTanks(fuelStation.getFuelTanks());
        response.setFuelPrices(fuelStation.getFuelPrices());
        response.setBuildingNumber(fuelStation.getAddress().buildingNumber());
        response.setCity(fuelStation.getAddress().city());
        response.setPostalCode(fuelStation.getAddress().postalCode());
        response.setCountry(fuelStation.getAddress().country());
        response.setAssignedManagersIds(fuelStation.getAssignedManagersIds());
        response.setStatus(fuelStation.getStatus());
        return response;
    }
    
    @JsonProperty("address")
    String getAddress() {
        return String.format("%s %s, %s %s, %s", street, buildingNumber, postalCode, city, country);
    }

}