package com.fuelstation.managmentapi.authentication.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.authentication.domain.CredentialsCreated;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Component
public class CredentialsEventHandler {

    private Logger logger = LoggerFactory.getLogger(CredentialsCreated.class);
    
    @EventListener
    public void handle(CredentialsCreated event) {
        logger.info("Credentials was created ID:" + event.getCredentialsId());

        // TODO move this from credentials 
        if (event.getRole() == UserRole.MANAGER) {
            // TODO implement id with EmailService and do not log credentials to console
            logger.info("Credentials was sent to the manager EMAIL:" + event.getEmail() + "PASSWORD:" + event.getPlainPassword());
            
        }
    }
}
