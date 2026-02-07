package com.fuelstation.managmentapi.fuelstation.domain.models;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelTank{
    private Long id;
    private FuelGrade fuelGrade;
    private float currentVolume;
    private float maxCapacity;
    private Optional<LocalDate> lastRefillDate;

    public float getAvailableVolume() {
        return this.maxCapacity - this.currentVolume;
    }
}