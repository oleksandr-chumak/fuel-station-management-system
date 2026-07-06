package com.fuelstation.managmentapi.fuelpurchase.application;

import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderConfirmed;
import com.fuelstation.managmentapi.fuelpurchase.application.usecases.CreateFuelPurchase;
import com.fuelstation.managmentapi.fuelpurchase.domain.events.FuelPurchaseCreated;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class FuelPurchaseEventHandler {

    private static final Logger log = LoggerFactory.getLogger(FuelPurchaseEventHandler.class);

    private final CreateFuelPurchase createFuelPurchase;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handle(FuelOrderConfirmed event) {
        try {
            createFuelPurchase.process(
                event.getFuelOrderId(),
                event.getFuelStationId(),
                event.getPricePerLiter(),
                event.getCurrency(),
                event.getPerformedBy()
            );
        } catch (Exception e) {
            log.error("Failed to create fuel purchase for order {} at station {}: {}",
                    event.getFuelOrderId(), event.getFuelStationId(), e.getMessage());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(FuelPurchaseCreated event) {
        log.info("Fuel purchase was created ID:{}", event.getFuelPurchaseId());
        messagingTemplate.convertAndSend("/topic/fuel-purchases", event);
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-purchases",
                event
        );
    }
}
