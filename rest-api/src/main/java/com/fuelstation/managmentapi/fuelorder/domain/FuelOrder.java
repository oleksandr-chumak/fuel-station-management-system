package com.fuelstation.managmentapi.fuelorder.domain;

import java.time.LocalDate;

import com.fuelstation.managmentapi.common.domain.AggregateRoot;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderConfirmed;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderRejected;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeConfirmedException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeRejectedException;

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
        if (status != FuelOrderStatus.PENDING) {
            throw new FuelOrderCannotBeConfirmedException(id, status);
        }
        status = FuelOrderStatus.CONFIRMED;
        pushDomainEvent(new FuelOrderConfirmed(id));
    }

    public void reject() {
        if (status != FuelOrderStatus.PENDING) {
            throw new FuelOrderCannotBeRejectedException(id, status);
        }
        status = FuelOrderStatus.REJECTED;
        pushDomainEvent(new FuelOrderRejected(id));
    }
}
