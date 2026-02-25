package com.fuelstation.managmentapi.authentication.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.authentication.domain.CredentialsCreated;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Component
public class CredentialsEventHandler {

    private final Logger logger = LoggerFactory.getLogger(CredentialsEventHandler.class);
    private final CredentialsEmailService credentialsEmailService;

    public CredentialsEventHandler(CredentialsEmailService credentialsEmailService) {
        this.credentialsEmailService = credentialsEmailService;
    }

    @EventListener
    public void handle(CredentialsCreated event) {
        logger.info("Credentials created ID:{}", event.getCredentialsId());

        if (event.getRole() == UserRole.MANAGER) {
            credentialsEmailService.sendManagerCredentials(event.getEmail(), event.getPlainPassword());
            logger.info("Credentials email sent to manager EMAIL:{}", event.getEmail());
        }
    }
}
