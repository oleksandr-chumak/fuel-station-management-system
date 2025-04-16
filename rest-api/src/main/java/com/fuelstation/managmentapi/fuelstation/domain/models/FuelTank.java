package com.fuelstation.managmentapi.fuelstation.domain.models;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
public class FuelTank{
    private Long id;
    private FuelGrade fuelGrade;
    private float currentVolume;
    private float maxCapacity;
    private Optional<LocalDate> lastRefilDate;

    public float getAvailableVolume() {
        return this.maxCapacity - this.currentVolume;
    }
}