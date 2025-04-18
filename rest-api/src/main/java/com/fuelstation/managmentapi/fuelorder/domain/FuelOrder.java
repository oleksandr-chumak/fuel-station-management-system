package com.fuelstation.managmentapi.fuelorder.domain;

import java.time.LocalDate;

import com.fuelstation.managmentapi.common.domain.AggregateRoot;
import com.fuelstation.managmentapi.common.domain.FuelGrade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FuelOrder extends AggregateRoot {
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
        pushDomainEvent(new FuelOrderWasConfirmed(id));
    }

    public void reject() {
        if (status != FuelOrderStatus.Pending) {
            throw new IllegalArgumentException("Cannot confirm fuel order because its current status is '" + status + "'. Only pending orders can be rejected.");
        }
        status = FuelOrderStatus.Rejected;
        pushDomainEvent(new FuelOrderWasRejected(id));
    }
}
