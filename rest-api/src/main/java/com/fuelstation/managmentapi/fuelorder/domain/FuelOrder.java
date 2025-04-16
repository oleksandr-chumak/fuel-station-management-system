package com.fuelstation.managmentapi.fuelorder.domain;

import java.time.LocalDate;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import lombok.Data;

@Data
public class FuelOrder {
    private Long id;
    private FuelOrderStatus status; 
    private FuelGrade grade;
    private float amount;
    private Long gasStationId; 
    private LocalDate createdAt;

    public void confirm() {
        status = FuelOrderStatus.Confirmed;
    }

    public void reject() {
        status = FuelOrderStatus.Rejected;
    }
}
