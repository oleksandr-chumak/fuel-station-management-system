package com.fuelstation.managmentapi.fuelstation.application.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFuelStationRequest {
    private String street;
    private String buildingNumber;
    private String city;
    private String postalCode;
    private String country;
}
