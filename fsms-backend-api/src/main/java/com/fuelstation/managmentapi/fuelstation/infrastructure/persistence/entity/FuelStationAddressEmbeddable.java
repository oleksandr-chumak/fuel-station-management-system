package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fuelstation.managmentapi.common.domain.CountryCode;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelStationAddressEmbeddable {
    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "building_number", nullable = false)
    private String buildingNumber;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "country", nullable = false)
    private CountryCode country;
}
