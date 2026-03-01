package com.fuelstation.managmentapi.fuelstation.infrastructure;

import com.fuelstation.managmentapi.authentication.application.UserFetcher;
import com.fuelstation.managmentapi.common.infrastructure.EmailService;
import com.fuelstation.managmentapi.manager.application.support.ManagerFetcher;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FuelStationEmailServiceImpl implements FuelStationEmailService {

    private final Logger logger = LoggerFactory.getLogger(FuelStationEmailServiceImpl.class);
    private final ManagerFetcher managerFetcher;
    private final UserFetcher userFetcher;
    private final EmailService emailService;

    @Override
    public void sendManagerAssigned(String to, String firstName, long fuelStationId) {
        try {
            var subject = "You have been assigned to a fuel station";
            var body = """
                    Hello %s,

                    You have been assigned to fuel station #%d.

                    Please log in to the Fuel Station Management System to view your assignment details.

                    """.formatted(firstName, fuelStationId);
            emailService.send(to, subject, body);
        } catch (Exception e) {
            logger.error("Failed to send manager-assigned email to '{}' for fuel station #{}: {}", to, fuelStationId, e.getMessage(), e);
        }
    }

    @Override
    public void sendManagerAssigned(long managerId, long fuelStationId) {
        try {
            var credentials = userFetcher.fetchById(managerId);
            var manager = managerFetcher.fetchById(managerId);
            this.sendManagerAssigned(credentials.getEmail(), manager.getFirstName(), fuelStationId);
        } catch (Exception e) {
            logger.error("Failed to send manager-assigned email for manager #{} and fuel station #{}: {}", managerId, fuelStationId, e.getMessage(), e);
        }
    }

    @Override
    public void sendManagerUnassigned(String to, String firstName, long fuelStationId) {
        try {
            var subject = "You have been unassigned from a fuel station";
            var body = """
                    Hello %s,

                    You have been unassigned from fuel station #%d.

                    Please log in to the Fuel Station Management System for more details.

                    """.formatted(firstName, fuelStationId);
            emailService.send(to, subject, body);
        } catch (Exception e) {
            logger.error("Failed to send manager-unassigned email to '{}' for fuel station #{}: {}", to, fuelStationId, e.getMessage(), e);
        }
    }

    @Override
    public void sendManagerUnassigned(long managerId, long fuelStationId) {
        try {
            var credentials = userFetcher.fetchById(managerId);
            var manager = managerFetcher.fetchById(managerId);
            this.sendManagerUnassigned(credentials.getEmail(), manager.getFirstName(), fuelStationId);
        } catch (Exception e) {
            logger.error("Failed to send manager-unassigned email for manager #{} and fuel station #{}: {}", managerId, fuelStationId, e.getMessage(), e);
        }
    }

}