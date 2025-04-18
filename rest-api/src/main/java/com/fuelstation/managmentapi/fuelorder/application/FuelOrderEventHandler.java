package com.fuelstation.managmentapi.fuelorder.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderWasConfirmed;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderWasCreated;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderWasRejected;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationService;

@Component
public class FuelOrderEventHandler {

    @Autowired
    private FuelStationService fuelStationService;

    private static final Logger logger = LoggerFactory.getLogger(FuelOrderEventHandler.class);

    @EventListener
    public void handle(FuelOrderWasCreated event) {
        logger.info("Fuel order was created ID:" + event.getFuelOrderId());
    }
    
    @EventListener
    public void handle(FuelOrderWasConfirmed event) {
        logger.info("Fuel order was confirmed ID:" + event.getFuelOrderId());
        fuelStationService.processFuelDelivery(event.getFuelOrderId());
    }

    @EventListener
    public void handle(FuelOrderWasRejected event) {
        logger.info("Fuel order was rejected ID" + event.getFuelOrderId());
    }
}
