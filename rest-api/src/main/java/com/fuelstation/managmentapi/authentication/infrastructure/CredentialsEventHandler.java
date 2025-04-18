package com.fuelstation.managmentapi.authentication.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.authentication.domain.CredentialsWasCreated;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Component
public class CredentialsEventHandler {

    private Logger logger = LoggerFactory.getLogger(CredentialsWasCreated.class);
    
    @EventListener
    public void handle(CredentialsWasCreated event) {
        logger.info("Credentials was created ID:" + event.getCredentialsId());

        if (event.getRole() == UserRole.Manager) {
            // TODO implement id with EmailService and do not log credentials to console
            logger.info("Credentials was sent to the manager EMAIL:" + event.getEmail() + "PASSWORD:" + event.getPlainPassword());
            
        }
    }
}
