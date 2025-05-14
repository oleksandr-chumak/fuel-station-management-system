package com.fuelstation.managmentapi.fuelstation.application.rest.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFuelStationRequest {

    @NotNull(message = "Street must not be null")
    private String street;

    @NotNull(message = "Building number must not be null")
    private String buildingNumber;

    @NotNull(message = "City must not be null")
    private String city;

    @NotNull(message = "Postal code must not be null")
    private String postalCode;

    @NotNull(message = "Country must not be null")
    private String country;

}
