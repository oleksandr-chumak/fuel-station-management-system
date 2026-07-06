package com.fuelstation.managmentapi.manager.application;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fuelstation.managmentapi.manager.domain.events.ManagerCreated;
import com.fuelstation.managmentapi.manager.domain.events.ManagerTerminated;

@Component
@AllArgsConstructor
public class ManagerEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ManagerEventHandler.class);
    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ManagerCreated event) {
        logger.info("Manager was created ID:{}", event.getManagerId());
        messagingTemplate.convertAndSend("/topic/managers/created", event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ManagerTerminated event) {
        logger.info("Manager was terminated ID:{}", event.getManagerId());
        messagingTemplate.convertAndSend("/topic/managers/" + event.getManagerId() + "/terminated", event);
    }

}