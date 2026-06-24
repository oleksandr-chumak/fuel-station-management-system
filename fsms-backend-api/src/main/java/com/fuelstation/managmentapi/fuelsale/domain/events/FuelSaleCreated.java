package com.fuelstation.managmentapi.fuelsale.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public final class FuelSaleCreated extends DomainEvent {

    private final FuelSaleEventType type = FuelSaleEventType.FUEL_SALE_CREATED;
    private final Long fuelSaleId;
    private final Long fuelStationId;
    private final Long fuelTankId;

    public FuelSaleCreated(Long fuelSaleId, Long fuelStationId, Long fuelTankId, Actor performedBy) {
        super(performedBy);
        this.fuelSaleId = fuelSaleId;
        this.fuelStationId = fuelStationId;
        this.fuelTankId = fuelTankId;
    }
}
