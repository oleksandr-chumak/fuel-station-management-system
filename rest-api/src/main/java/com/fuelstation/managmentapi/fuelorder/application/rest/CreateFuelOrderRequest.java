package com.fuelstation.managmentapi.fuelorder.application.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFuelOrderRequest {
    private Long gasStationId;
    private String fuelGrade;
    private float amount;
}