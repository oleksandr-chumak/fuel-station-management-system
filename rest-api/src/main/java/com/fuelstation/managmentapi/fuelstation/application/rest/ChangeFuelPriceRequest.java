package com.fuelstation.managmentapi.fuelstation.application.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeFuelPriceRequest {
    private String fuelGrade;
    private float newPrice;
}