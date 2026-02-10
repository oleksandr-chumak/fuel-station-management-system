package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

public record FuelOrderConfirmed(long fuelOrderId) implements DomainEvent {
}
