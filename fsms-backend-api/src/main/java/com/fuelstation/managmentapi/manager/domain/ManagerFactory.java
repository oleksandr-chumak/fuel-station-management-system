package com.fuelstation.managmentapi.manager.domain;

public interface ManagerFactory {
    Manager create(long credentialsId, String firstName, String lastName);
}
