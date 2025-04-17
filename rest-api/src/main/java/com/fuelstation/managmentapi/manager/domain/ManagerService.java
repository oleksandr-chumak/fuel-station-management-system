package com.fuelstation.managmentapi.manager.domain;

public interface ManagerService {
    public Manager createManager(String firstName, String lastName, String email);
    public Manager terminateManager(Long managerId);
}
