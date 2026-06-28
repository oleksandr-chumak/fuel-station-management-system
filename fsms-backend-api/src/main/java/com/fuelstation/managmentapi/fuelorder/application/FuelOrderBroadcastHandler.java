package com.fuelstation.managmentapi.fuelorder.application;

import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderConfirmed;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderCreated;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderProcessed;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderRejected;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class FuelOrderBroadcastHandler {

    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(FuelOrderCreated event) {
        messagingTemplate.convertAndSend("/topic/fuel-orders/created", event);
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-order-created",
                event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(FuelOrderConfirmed event) {
        messagingTemplate.convertAndSend(
                "/topic/fuel-orders/" + event.getFuelOrderId() + "/confirmed",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-order-confirmed",
                event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(FuelOrderRejected event) {
        messagingTemplate.convertAndSend(
                "/topic/fuel-orders/" + event.getFuelOrderId() + "/rejected",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-order-rejected",
                event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void broadcast(FuelOrderProcessed event) {
        messagingTemplate.convertAndSend(
                "/topic/fuel-orders/" + event.getFuelOrderId() + "/processed",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-order-processed",
                event
        );
    }
}
