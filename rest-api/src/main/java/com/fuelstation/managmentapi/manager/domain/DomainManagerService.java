package com.fuelstation.managmentapi.manager.domain;

import java.security.SecureRandom;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.CredentialsService;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;

@Service
public class DomainManagerService implements ManagerService {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired  
    private CredentialsService credentialsService;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public Manager createManager(String firstName, String lastName, String email) {

        Credentials credentials = credentialsService.createCredentials(email, generateRandomPassword(10), UserRole.Manager);
        Manager manger = managerRepository.save(new Manager(null, firstName, lastName, ManagerStatus.Active, credentials.getId()));
        domainEventPublisher.publish(new ManagerWasCreated(manger.getId()));
        return manger;
    }

    @Override
    public Manager terminateManager(Long managerId) {
        Manager manger = managerRepository.findById(managerId).orElseThrow(() -> new NoSuchElementException("Manager with id:" + managerId + "doesn't exist"));
        manger.terminate();
        managerRepository.save(manger);
        domainEventPublisher.publishAll(manger.getDomainEvents());
        return manger;
    }
    
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUV456WXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}
