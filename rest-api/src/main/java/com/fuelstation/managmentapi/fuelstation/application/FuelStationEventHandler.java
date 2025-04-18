package com.fuelstation.managmentapi.fuelstation.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.events.FuelDeliveryWasProcessed;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelPriceWasChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationWasCreated;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationWasDeactivated;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerWasAssignedToFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerWasUnassignedFromFuelStation;

@Component
public class FuelStationEventHandler {

    private Logger logger = LoggerFactory.getLogger(FuelStationEventHandler.class);
    
    @EventListener
    public void handle(FuelStationWasCreated event) {
        logger.info("Fuel station was created ID:" + event.getFuelStationId());
    }
    
    @EventListener
    public void handle(FuelDeliveryWasProcessed event) {
        logger.info("Fuel delivery was processed ID:" + event.getFuelOrderId());
    }
    
    @EventListener
    public void handle(FuelPriceWasChanged event) {
        logger.info("Fuel price was changed ID:" + event.getFuelStationId());
    }
    
    @EventListener
    public void handle(FuelStationWasDeactivated event) {
        logger.info("Fuel station was deactivated ID:" + event.getFuelStationId());
    }
    
    @EventListener
    public void handle(ManagerWasAssignedToFuelStation event) {
        logger.info("Manager was assigned to fuel station ID:" + event.getFuelStationId() + " MANAGER ID:" + event.getManagerId());
    }
    
    @EventListener
    public void handle(ManagerWasUnassignedFromFuelStation event) {
        logger.info("Manager was unassigned from fuel station ID:" + event.getFuelStationId() + " MANAGER ID:" + event.getManagerId());
    }
}        
