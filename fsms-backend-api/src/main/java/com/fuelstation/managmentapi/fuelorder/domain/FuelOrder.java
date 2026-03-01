package com.fuelstation.managmentapi.fuelorder.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.AggregateRoot;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderConfirmed;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderProcessed;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderRejected;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeConfirmedException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeProcessedException;
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
    private Long fuelOrderId;
    private FuelOrderStatus status; 
    private FuelGrade grade;
    private BigDecimal amount;
    private Long fuelStationId; 
    private OffsetDateTime createdAt;

    public void confirm(Actor performedBy) {
        if (status != FuelOrderStatus.PENDING) {
            throw new FuelOrderCannotBeConfirmedException(fuelOrderId, status);
        }
        status = FuelOrderStatus.CONFIRMED;
        pushDomainEvent(new FuelOrderConfirmed(fuelOrderId, fuelStationId, performedBy));
    }

    public void reject(Actor performedBy) {
        if (status != FuelOrderStatus.PENDING) {
            throw new FuelOrderCannotBeRejectedException(fuelOrderId, status);
        }
        status = FuelOrderStatus.REJECTED;
        pushDomainEvent(new FuelOrderRejected(fuelOrderId, fuelStationId, performedBy));
    }

    public void process(Actor performedBy) {
        if (status != FuelOrderStatus.CONFIRMED) {
            throw new FuelOrderCannotBeProcessedException(fuelOrderId, status);
        }
        status = FuelOrderStatus.PROCESSED;
        pushDomainEvent(new FuelOrderProcessed(fuelOrderId, fuelStationId, performedBy));
    }
}
