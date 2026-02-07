package com.fuelstation.managmentapi.manager.domain;

public interface ManagerFactory {
    public Manager create(String firstName, String lastName, long credentialsId);
}
