package com.fuelstation.managmentapi.fuelstation.application.rest.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFuelStationRequest {

    @NotBlank(message = "Street must not be blank")
    @Size(min = 2, max = 100, message = "Street must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.,']+$", message = "Street contains invalid characters")
    private String street;

    @NotBlank(message = "Building number must not be blank")
    @Size(min = 1, max = 20, message = "Building number must be between 1 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\-/]+$", message = "Building number can only contain letters, numbers, hyphens, and slashes")
    private String buildingNumber;

    @NotBlank(message = "City must not be blank")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\-']+$", message = "City can only contain letters, spaces, hyphens, and apostrophes")
    private String city;

    @NotBlank(message = "Postal code must not be blank")
    @Pattern(
            regexp = "^(?:[0-9]{4,6}|[A-Za-z][0-9][A-Za-z]\\s?[0-9][A-Za-z][0-9]|[0-9]{5}-[0-9]{4})$",
            message = "Postal code format is invalid. Supported formats: 12345, 12345-6789, A1A 1A1, A1A1A1"
    )
    private String postalCode;

    @NotBlank(message = "Country must not be blank")
    @Size(min = 2, max = 60, message = "Country must be between 2 and 60 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\-']+$", message = "Country can only contain letters, spaces, hyphens, and apostrophes")
    private String country;
}
