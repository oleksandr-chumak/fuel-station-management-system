package com.fuelstation.managmentapi.fuelstation.application;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.events.FuelPriceChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationCreated;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationDeactivated;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerAssignedToFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerUnassignedFromFuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.FuelStationEmailService;

@Component
@AllArgsConstructor
public class FuelStationEventHandler {

    private final Logger logger = LoggerFactory.getLogger(FuelStationEventHandler.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final FuelStationEmailService fuelStationEmailService;

    @EventListener
    public void handle(FuelStationCreated event) {
        logger.info("Fuel station was created ID:{}", event.getFuelStationId());
        messagingTemplate.convertAndSend("/topic/fuel-stations/created", event);
    }
    
    @EventListener
    public void handle(FuelPriceChanged event) {
        logger.info("Fuel price was changed ID:{}", event.getFuelStationId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-price-changed",
                event
        );
    }

    @EventListener
    public void handle(FuelStationDeactivated event) {
        logger.info("Fuel station was deactivated ID:{}", event.getFuelStationId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/deactivated",
                event
        );
    }
    
    @EventListener
    public void handle(ManagerAssignedToFuelStation event) {
        logger.info("Manager was assigned to fuel station ID:{} MANAGER ID:{}", event.getFuelStationId(), event.getManagerId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/manager-assigned",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/managers/" + event.getManagerId() + "/assigned-to-fuel-station",
                event
        );
        this.fuelStationEmailService.sendManagerAssigned(event.getManagerId(), event.getFuelStationId());
    }

    @EventListener
    public void handle(ManagerUnassignedFromFuelStation event) {
        logger.info("Manager was unassigned from fuel station ID:{} MANAGER ID:{}", event.getFuelStationId(), event.getManagerId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/manager-unassigned",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/managers/" + event.getManagerId() + "/unassigned-from-fuel-station",
                event
        );
        this.fuelStationEmailService.sendManagerUnassigned(event.getManagerId(), event.getFuelStationId());
    }

}
