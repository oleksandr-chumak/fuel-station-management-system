package com.fuelstation.managmentapi.authentication.infrastructure;

import com.fuelstation.managmentapi.common.infrastructure.EmailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserEmailServiceImpl implements UserEmailService {

    private final Logger logger = LoggerFactory.getLogger(UserEmailServiceImpl.class);
    private final EmailService emailService;

    @Override
    public void sendManagerCredentials(String to, String password) {
        try {
            var subject = "Your Manager Account Credentials";
            var body = """
                    Welcome to Fuel Station Management System!
                    
                    Your manager account has been created. Use the user below to log in:
                    
                      Email:    %s
                      Password: %s
                    
                    """.formatted(to, password);
            emailService.send(to, subject, body);
        } catch (Exception e) {
            logger.error("Failed to send manager user email to '{}': {}", to, e.getMessage(), e);
        }
    }
}