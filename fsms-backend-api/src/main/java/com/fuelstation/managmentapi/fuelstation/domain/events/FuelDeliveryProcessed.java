    package com.fuelstation.managmentapi.fuelstation.domain.events;
    
    import com.fuelstation.managmentapi.common.domain.DomainEvent;
    
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.Getter;
    
    // TODO it should not be a FuelStationEventType i think. Rename to fuel order processed
    @Getter
    public class FuelDeliveryProcessed implements DomainEvent {
        private final FuelStationEventType type = FuelStationEventType.FUEL_STATION_FUEL_DELIVERY_PROCESSED;
        private final long fuelOrderId;
    
        public FuelDeliveryProcessed(long fuelOrderId) {
            this.fuelOrderId = fuelOrderId;
        }
    }
