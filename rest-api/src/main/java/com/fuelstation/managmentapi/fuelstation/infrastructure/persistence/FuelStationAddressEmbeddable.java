package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelStationAddressEmbeddable {
    private String street;
    private String buildingNumber;
    private String city;
    private String postalCode;
    private String country;
}
