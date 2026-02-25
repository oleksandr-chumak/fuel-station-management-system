package com.fuelstation.managmentapi.manager.application;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.events.ManagerCreated;
import com.fuelstation.managmentapi.manager.domain.events.ManagerTerminated;

@Component
@AllArgsConstructor
public class ManagerEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ManagerEventHandler.class);
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handle(ManagerCreated event) {
        logger.info("Manager was created ID:{}", event.managerId());
        messagingTemplate.convertAndSend("/topic/managers/created", event);
    }

    @EventListener
    public void handle(ManagerTerminated event) {
        logger.info("Manager was terminated ID:{}", event.mangerId());
        messagingTemplate.convertAndSend("/topic/managers/" + event.mangerId() + "/terminated", event);
    }

}