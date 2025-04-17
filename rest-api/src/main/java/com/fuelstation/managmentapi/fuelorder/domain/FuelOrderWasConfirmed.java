package com.fuelstation.managmentapi.fuelorder.domain;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FuelOrderWasConfirmed implements DomainEvent {
    private Long fuelOrderId;
}
