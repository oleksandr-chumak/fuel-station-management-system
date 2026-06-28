package com.fuelstation.managmentapi.fuelorder.application;

import com.fuelstation.managmentapi.fuelorder.application.usecases.CreateFuelOrderEvent;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderConfirmed;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderCreated;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderProcessed;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderRejected;
import com.fuelstation.managmentapi.fuelstation.application.usecases.ProcessFuelDelivery;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FuelOrderEventHandler {

    private final ProcessFuelDelivery processFuelDelivery;
    private final CreateFuelOrderEvent createFuelOrderEvent;

    private static final Logger logger = LoggerFactory.getLogger(FuelOrderEventHandler.class);

    @EventListener
    public void handle(FuelOrderCreated event) {
        logger.info("Fuel order was created ID:{}", event.getFuelOrderId());
        createFuelOrderEvent.process(event);
    }

    @EventListener
    public void handle(FuelOrderConfirmed event) {
        logger.info("Fuel order was confirmed ID:{}", event.getFuelOrderId());
        createFuelOrderEvent.process(event);
        processFuelDelivery.process(event.getFuelOrderId());
    }

    @EventListener
    public void handle(FuelOrderRejected event) {
        logger.info("Fuel order was rejected ID:{}", event.getFuelOrderId());
        createFuelOrderEvent.process(event);
    }

    @EventListener
    public void handle(FuelOrderProcessed event) {
        logger.info("Fuel order was processed ID:{}", event.getFuelOrderId());
        createFuelOrderEvent.process(event);
    }
}
