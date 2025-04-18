package com.fuelstation.managmentapi.manager.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.ManagerWasCreated;
import com.fuelstation.managmentapi.manager.domain.ManagerWasTerminated;

@Component
public class ManagerEventHandler {

    private static Logger logger = LoggerFactory.getLogger(ManagerEventHandler.class); 
    
    @EventListener
    public void handle(ManagerWasTerminated event) {
        logger.info("Manager was terminated ID:" + event.getMangerId());
    }
    
    @EventListener
    public void handle(ManagerWasCreated event) {
        logger.info("Manager was created ID:" + event.getMangerId());
    }
}
