package com.fuelstation.managmentapi.fuelstation.application.rest;

import java.util.List;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;

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
    private String country;
    private boolean active;
    
    public static FuelStationResponse fromDomain(FuelStation fuelStation) {
        FuelStationResponse response = new FuelStationResponse();
        response.setId(fuelStation.getId());
        response.setStreet(fuelStation.getAddress().street());
        response.setBuildingNumber(fuelStation.getAddress().buildingNumber());
        response.setCity(fuelStation.getAddress().city());
        response.setPostalCode(fuelStation.getAddress().postalCode());
        response.setCountry(fuelStation.getAddress().country());
        response.setAssignedManagersIds(fuelStation.getAssignedManagersIds());
        response.setActive(fuelStation.getStatus() == FuelStationStatus.Active);
        return response;
    }

}