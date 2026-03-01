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

import java.util.Map;

@Component
@AllArgsConstructor
public class FuelOrderEventHandler {

    private ProcessFuelDelivery processFuelDelivery;

    private static final Logger logger = LoggerFactory.getLogger(FuelOrderEventHandler.class);
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handle(FuelOrderCreated event) {
        logger.info("Fuel order was created ID:{}", event.getFuelOrderId());
        messagingTemplate.convertAndSend("/topic/fuel-orders/created", event);
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-order-created",
                event
        );
/*
        eventLogger.log(event.getFuelStationId(), FuelOrderEventType.FUEL_ORDER_CREATED.name(), Map.of(
                "fuelOrderId", event.getFuelOrderId()
        ));
*/
    }

    @EventListener
    public void handle(FuelOrderConfirmed event) {
        logger.info("Fuel order was confirmed ID:{}", event.getFuelOrderId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-orders/" + event.getFuelOrderId() + "/confirmed",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-order-confirmed",
                event
        );
        processFuelDelivery.process(event.getFuelOrderId());
/*
        eventLogger.log(event.getFuelStationId(), FuelOrderEventType.FUEL_ORDER_CONFIRMED.name(), Map.of(
                "fuelOrderId", event.getFuelOrderId()
        ));
*/
    }

    @EventListener
    public void handle(FuelOrderRejected event) {
        logger.info("Fuel order was rejected ID{}", event.getFuelOrderId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-orders/" + event.getFuelOrderId() + "/rejected",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-order-rejected",
                event
        );
/*
        eventLogger.log(event.getFuelStationId(), FuelOrderEventType.FUEL_ORDER_REJECTED.name(), Map.of(
                "fuelOrderId", event.getFuelOrderId()
        ));
*/
    }

    @EventListener
    public void handle(FuelOrderProcessed event) {
        logger.info("Fuel order was processed ID:{}", event.getFuelOrderId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-orders/" + event.getFuelOrderId() + "/processed",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-order-processed",
                event
        );
/*
        eventLogger.log(event.getFuelStationId(), FuelOrderEventType.FUEL_ORDER_PROCESSED.name(), Map.of(
                "fuelOrderId", event.getFuelOrderId()
        ));
*/
    }
}
