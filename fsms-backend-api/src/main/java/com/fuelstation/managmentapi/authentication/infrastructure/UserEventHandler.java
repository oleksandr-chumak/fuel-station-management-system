package com.fuelstation.managmentapi.authentication.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.authentication.domain.UserCreated;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Component
public class UserEventHandler {

    private final Logger logger = LoggerFactory.getLogger(UserEventHandler.class);
    private final UserEmailService userEmailService;

    public UserEventHandler(UserEmailService userEmailService) {
        this.userEmailService = userEmailService;
    }

    @EventListener
    public void handle(UserCreated event) {
        logger.info("Credentials created ID:{}", event.getUserId());

        if (event.getRole() == UserRole.MANAGER) {
            userEmailService.sendManagerCredentials(event.getEmail(), event.getPlainPassword());
            logger.info("Credentials email sent to manager EMAIL:{}", event.getEmail());
        }
    }
}
