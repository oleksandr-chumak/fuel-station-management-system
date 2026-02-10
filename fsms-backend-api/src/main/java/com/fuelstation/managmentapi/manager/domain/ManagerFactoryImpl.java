package com.fuelstation.managmentapi.manager.domain;

import org.springframework.stereotype.Component;

@Component
public class ManagerFactoryImpl implements ManagerFactory {

    @Override
    public Manager create(long credentialsId, String firstName, String lastName) {
        return new Manager(credentialsId, firstName, lastName, ManagerStatus.ACTIVE);
    }

}
