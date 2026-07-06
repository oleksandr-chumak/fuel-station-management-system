package com.fuelstation.managmentapi.fuelstation.application.stomp;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fuelstation.managmentapi.fuelstation.domain.events.FuelPriceChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationCreated;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationDeactivated;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelTankDecommissioned;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelTankInstalled;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerAssignedToFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerUnassignedFromFuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.FuelStationEmailService;

@Component
@AllArgsConstructor
public class FuelStationBroadcastHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final FuelStationEmailService fuelStationEmailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(FuelStationCreated event) {
        messagingTemplate.convertAndSend("/topic/fuel-stations/created", event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(FuelPriceChanged event) {
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-price-changed",
                event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(FuelStationDeactivated event) {
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/deactivated",
                event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(ManagerAssignedToFuelStation event) {
        fuelStationEmailService.sendManagerAssigned(event.getManagerId(), event.getFuelStationId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/manager-assigned",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/managers/" + event.getManagerId() + "/assigned-to-fuel-station",
                event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(ManagerUnassignedFromFuelStation event) {
        fuelStationEmailService.sendManagerUnassigned(event.getManagerId(), event.getFuelStationId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/manager-unassigned",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/managers/" + event.getManagerId() + "/unassigned-from-fuel-station",
                event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(FuelTankInstalled event) {
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-tank-installed",
                event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(FuelTankDecommissioned event) {
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-tank-decommissioned",
                event
        );
    }
}
