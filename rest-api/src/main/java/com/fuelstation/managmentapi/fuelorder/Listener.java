package com.fuelstation.managmentapi.fuelorder;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderWasConfirmed;

@Component
public class Listener {
    
    @EventListener
    public void handleFuelOrderWasConfirmedEvent(FuelOrderWasConfirmed event) {
        System.out.print("Fuel order was confirmed");
    }
}
