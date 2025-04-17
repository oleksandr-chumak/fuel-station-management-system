package com.fuelstation.managmentapi.fuelorder.domain;

import java.time.LocalDate;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelOrder {
    private Long id;
    private FuelOrderStatus status; 
    private FuelGrade grade;
    private float amount;
    private Long fuelStationId; 
    private LocalDate createdAt;

    public void confirm() {
        if (status != FuelOrderStatus.Pending) {
            throw new IllegalArgumentException("Cannot confirm fuel order because its current status is '" + status + "'. Only pending orders can be confirmed.");
        }
        status = FuelOrderStatus.Confirmed;
    }

    public void reject() {
        if (status != FuelOrderStatus.Pending) {
            throw new IllegalArgumentException("Cannot confirm fuel order because its current status is '" + status + "'. Only pending orders can be rejected.");
        }
        status = FuelOrderStatus.Rejected;
    }
}
