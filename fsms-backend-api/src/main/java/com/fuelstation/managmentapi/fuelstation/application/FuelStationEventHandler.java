package com.fuelstation.managmentapi.fuelstation.application;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.events.FuelDeliveryProcessed;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelPriceChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationCreated;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationDeactivated;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerAssignedToFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerUnassignedFromFuelStation;

@Component
@AllArgsConstructor
public class FuelStationEventHandler {

    private final Logger logger = LoggerFactory.getLogger(FuelStationEventHandler.class);
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handle(FuelStationCreated event) {
        logger.info("Fuel station was created ID:{}", event.getFuelStationId());
        messagingTemplate.convertAndSend("/topic/fuel-stations/created", event);
    }
    
    @EventListener
    public void handle(FuelDeliveryProcessed event) {
        logger.info("Fuel delivery was processed ID:{}", event.getFuelOrderId());
    }
    
    @EventListener
    public void handle(FuelPriceChanged event) {
        logger.info("Fuel price was changed ID:{}", event.getFuelStationId());
    }

    @EventListener
    public void handle(FuelStationDeactivated event) {
        logger.info("Fuel station was deactivated ID:{}", event.getFuelStationId());
    }
    
    @EventListener
    public void handle(ManagerAssignedToFuelStation event) {
        logger.info("Manager was assigned to fuel station ID:{} MANAGER ID:{}", event.getFuelStationId(), event.getManagerId());
    }
    
    @EventListener
    public void handle(ManagerUnassignedFromFuelStation event) {
        logger.info("Manager was unassigned from fuel station ID:{} MANAGER ID:{}", event.getFuelStationId(), event.getManagerId());
    }
}        
