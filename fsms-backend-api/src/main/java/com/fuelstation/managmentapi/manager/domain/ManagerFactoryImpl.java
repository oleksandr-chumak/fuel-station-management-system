package com.fuelstation.managmentapi.manager.domain;

import org.springframework.stereotype.Component;

@Component
public class ManagerFactoryImpl implements ManagerFactory {

    @Override
    public Manager create(String firstName, String lastName, long credentialsId) {
        return new Manager(null, firstName, lastName, ManagerStatus.ACTIVE, credentialsId);
    }

}
