package com.fuelstation.managmentapi.fuelstation.application.rest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelStationResponse {
    private Long fuelStationId;
    private String street;
    private String buildingNumber;
    private String city;
    private String postalCode;
    private List<Long> assignedManagersIds;
    private List<FuelTankResponse> fuelTanks;
    private List<FuelPriceResponse> fuelPrices;
    private String country;
    private String status;

    public static FuelStationResponse fromDomain(FuelStation fuelStation) {
        FuelStationResponse response = new FuelStationResponse();
        response.setFuelStationId(fuelStation.getFuelStationId());
        response.setStreet(fuelStation.getAddress().street());
        response.setFuelTanks(
                fuelStation.getFuelTanks()
                        .stream()
                        .map(t -> new FuelTankResponse(
                                t.getId(),
                                t.getFuelGrade().toString(),
                                t.getCurrentVolume(),
                                t.getMaxCapacity(),
                                t.getLastRefillDate()))
                        .toList());
        response.setFuelPrices(
                fuelStation.getFuelPrices()
                        .stream()
                        .map(p -> new FuelPriceResponse(
                                p.fuelGrade().toString(),
                                p.pricePerLiter()))
                        .toList());
        response.setBuildingNumber(fuelStation.getAddress().buildingNumber());
        response.setCity(fuelStation.getAddress().city());
        response.setPostalCode(fuelStation.getAddress().postalCode());
        response.setCountry(fuelStation.getAddress().country());
        response.setAssignedManagersIds(fuelStation.getAssignedManagersIds());
        response.setStatus(fuelStation.getStatus().toString());
        return response;
    }

    @JsonProperty(value = "address")
    String getAddress() {
        return String.format("%s %s, %s %s, %s", street, buildingNumber, postalCode, city, country);
    }

    private record FuelTankResponse(
            Long id,
            String fuelGrade,
            BigDecimal currentVolume,
            BigDecimal maxCapacity,
            Optional<OffsetDateTime> lastRefillDate) {
    }

    public record FuelPriceResponse(String fuelGrade, BigDecimal pricePerLiter) {
    }

}