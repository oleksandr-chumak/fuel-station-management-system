package com.fuelstation.managmentapi.fuelorder.application;

import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderProcessed;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderCreated;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderConfirmed;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderRejected;
import com.fuelstation.managmentapi.fuelstation.application.usecases.ProcessFuelDelivery;

@Component
@AllArgsConstructor
public class FuelOrderEventHandler {

    private ProcessFuelDelivery processFuelDelivery;

    private static final Logger logger = LoggerFactory.getLogger(FuelOrderEventHandler.class);
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handle(FuelOrderCreated event) {
        logger.info("Fuel order was created ID:{}", event.fuelOrderId());
        messagingTemplate.convertAndSend("/topic/fuel-orders/created", event);
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.fuelStationId() + "/fuel-order-created",
                event
        );
    }

    @EventListener
    public void handle(FuelOrderConfirmed event) {
        logger.info("Fuel order was confirmed ID:{}", event.fuelOrderId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-orders/" + event.fuelOrderId() + "/confirmed",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.fuelStationId() + "/fuel-order-confirmed",
                event
        );
        processFuelDelivery.process(event.fuelOrderId());
    }

    @EventListener
    public void handle(FuelOrderRejected event) {
        logger.info("Fuel order was rejected ID{}", event.fuelOrderId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-orders/" + event.fuelOrderId() + "/rejected",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.fuelStationId() + "/fuel-order-rejected",
                event
        );
    }

    @EventListener
    public void handle(FuelOrderProcessed event) {
        logger.info("Fuel order was processed ID:{}", event.fuelOrderId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-orders/" + event.fuelOrderId() + "/processed",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.fuelStationId() + "/fuel-order-processed",
                event
        );
    }
}
